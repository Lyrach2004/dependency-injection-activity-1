package net.lyrach.core;

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

            // 1. Injection par constructeur
            if (!def.getConstructorArgs().isEmpty()) {

                Class<?>[] paramTypes = new Class<?>[def.getConstructorArgs().size()];
                Object[] args = new Object[def.getConstructorArgs().size()];

                for (int i = 0; i < def.getConstructorArgs().size(); i++) {
                    var arg = def.getConstructorArgs().get(i);
                    Object dependency = getBean(arg.ref);

                    paramTypes[i] = dependency.getClass();
                    args[i] = dependency;
                }

                bean = clazz.getConstructor(paramTypes).newInstance(args);
            } else {
                // Constructeur vide
                bean = clazz.getDeclaredConstructor().newInstance();
            }

            // 2. Injection par setter (version corrigée)
            for (var prop : def.getProperties()) {
                Object dependency = getBean(prop.ref);
                String setterName = "set" + capitalize(prop.name);

                boolean injected = false;

                for (var method : clazz.getMethods()) {
                    if (method.getName().equals(setterName)
                            && method.getParameterCount() == 1
                            && method.getParameterTypes()[0].isAssignableFrom(dependency.getClass())) {

                        method.invoke(bean, dependency);
                        injected = true;
                        break;
                    }
                }

                if (!injected) {
                    throw new RuntimeException("Setter introuvable : " + setterName);
                }
            }

            // 3. Injection par field
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




}
