package net.lyrach.core;

import net.lyrach.annotation.Autowired;
import net.lyrach.annotation.Component;
import net.lyrach.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

public class SimpleApplicationContext implements BeanFactory {

    private Map<String,Object> beans = new HashMap<String,Object>();
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    public void registerBeanDefinition(BeanDefinition def) {
        beanDefinitions.put(def.getId(), def);
    }


    public void registerBean(String name,Object bean){
        this.beans.put(name,bean);
    }

    public Object createBean(String className){
        try{
            Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredConstructor().newInstance();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public Object createBeanWithConstructor(String className, Class<?>[] paramTypes, Object[] args) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getConstructor(paramTypes).newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void injectSetter(Object bean, String methodName, Object dependency) {
        try {
            var method = bean.getClass().getMethod(methodName, dependency.getClass());
            method.invoke(bean, dependency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void injectField(Object bean, String fieldName, Object dependency) {
        try {
            var field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(bean, dependency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object createBeanFromDefinition(BeanDefinition def) {
        try {
            Class<?> clazz = Class.forName(def.getClassName());
            Object bean;

            // ============================
            // 1. Injection par constructeur
            // ============================
            if (!def.getConstructorArgs().isEmpty()) {

                Object[] args = new Object[def.getConstructorArgs().size()];

                for (int i = 0; i < def.getConstructorArgs().size(); i++) {
                    var arg = def.getConstructorArgs().get(i);
                    args[i] = getBean(arg.ref);
                }

                // Recherche d’un constructeur compatible
                for (var constructor : clazz.getConstructors()) {
                    if (constructor.getParameterCount() == args.length) {

                        boolean compatible = true;

                        for (int i = 0; i < args.length; i++) {
                            if (!constructor.getParameterTypes()[i].isAssignableFrom(args[i].getClass())) {
                                compatible = false;
                                break;
                            }
                        }

                        if (compatible) {
                            bean = constructor.newInstance(args);
                            return bean; // IMPORTANT : on sort ici
                        }
                    }
                }

                throw new RuntimeException("Aucun constructeur compatible trouvé pour " + def.getId());
            }

            // ============================
            // 2. Constructeur vide
            // ============================
            bean = clazz.getDeclaredConstructor().newInstance();

            // ============================
            // 3. Injection via setters
            // ============================
            for (var setter : def.getSetters()) {
                Object dependency = getBean(setter.ref);

                // On récupère la méthode
                var methods = clazz.getMethods();
                boolean injected = false;

                for (var method : methods) {
                    if (method.getName().equals(setter.method)
                            && method.getParameterCount() == 1
                            && method.getParameterTypes()[0].isAssignableFrom(dependency.getClass())) {

                        method.invoke(bean, dependency);
                        injected = true;
                        break;
                    }
                }

                if (!injected) {
                    throw new RuntimeException("Setter introuvable : " + setter.method);
                }
            }

            // ============================
            // 4. Injection via fields
            // ============================
            for (var fieldArg : def.getFields()) {
                Object dependency = getBean(fieldArg.ref);

                var field = clazz.getDeclaredField(fieldArg.name);
                field.setAccessible(true);
                field.set(bean, dependency);
            }

            return bean;

        } catch (Exception e) {
            throw new RuntimeException("Erreur création bean : " + def.getId(), e);
        }
    }




    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public void loadBeanDefinitions(Map<String, BeanDefinition> defs) {
        this.beanDefinitions.putAll(defs);
    }

    @Override
    public Object getBean(String name) {
        // 1. Si le bean existe déjà → le retourner
        if (beans.containsKey(name)) {
            return beans.get(name);
        }

        // 2. Sinon → chercher sa BeanDefinition
        BeanDefinition def = beanDefinitions.get(name);
        if (def == null) {
            throw new RuntimeException("Aucune BeanDefinition trouvée pour : " + name);
        }

        // 3. Créer le bean
        Object bean = createBeanFromDefinition(def);

        // 4. Le stocker dans le cache
        beans.put(name, bean);

        return bean;
    }

    public void loadAnnotatedBeans(String basePackage) {
        ClassPathScanner scanner = new ClassPathScanner();
        var classes = scanner.scan(basePackage);

        for (Class<?> clazz : classes) {
            Component comp = clazz.getAnnotation(Component.class);

            String id = comp.value().isEmpty()
                    ? clazz.getSimpleName().substring(0,1).toLowerCase() + clazz.getSimpleName().substring(1)
                    : comp.value();

            BeanDefinition def = new BeanDefinition(id, clazz.getName());

            // ============================
            // Injection via @Autowired (fields)
            // ============================
            for (var field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    BeanDefinition.FieldArg f = new BeanDefinition.FieldArg();
                    f.name = field.getName();

                    if (field.isAnnotationPresent(Qualifier.class)) {
                        f.ref = field.getAnnotation(Qualifier.class).value();
                    } else {
                        f.ref = field.getName();
                    }

                    def.getFields().add(f);
                }
            }

            // ============================
            // Injection via @Autowired (constructeur)
            // ============================
            for (var constructor : clazz.getDeclaredConstructors()) {
                if (constructor.isAnnotationPresent(Autowired.class)) {

                    var params = constructor.getParameters();

                    for (var param : params) {
                        BeanDefinition.ConstructorArg c = new BeanDefinition.ConstructorArg();

                        if (param.isAnnotationPresent(Qualifier.class)) {
                            c.ref = param.getAnnotation(Qualifier.class).value();
                        } else {
                            c.ref = param.getName(); // grâce à -parameters
                        }

                        def.getConstructorArgs().add(c);
                    }
                }
            }

            // ============================
            // Injection via @Autowired (setter)
            // ============================
            for (var method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Autowired.class)) {

                    // Vérifier que c’est un setter
                    if (method.getParameterCount() != 1 || !method.getName().startsWith("set")) {
                        throw new RuntimeException("@Autowired ne peut être appliqué qu'à un setter : " + method);
                    }

                    BeanDefinition.SetterArg s = new BeanDefinition.SetterArg();
                    s.method = method.getName();

                    var param = method.getParameters()[0];

                    if (param.isAnnotationPresent(Qualifier.class)) {
                        s.ref = param.getAnnotation(Qualifier.class).value();
                    } else {
                        s.ref = param.getName();
                    }

                    def.getSetters().add(s);
                }
            }

            // ============================
            // Vérification des doublons
            // ============================
            if (beanDefinitions.containsKey(id)) {
                throw new RuntimeException(
                        "Conflit de beans : plusieurs classes utilisent le même nom de bean '" + id + "'"
                );
            }

            beanDefinitions.put(id, def);
        }
    }





}
