package net.lyrach.core;

import java.util.ArrayList;
import java.util.List;

public class BeanDefinition {

    private String id;
    private String className;

    // Injection par constructeur
    private List<ConstructorArg> constructorArgs = new ArrayList<>();

    // Injection par setter via @Autowired
    private List<SetterArg> setters = new ArrayList<>();

    // Injection par setter via configuration (properties)
    private List<PropertyArg> properties = new ArrayList<>();

    // Injection par field
    private List<FieldArg> fields = new ArrayList<>();

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public String getId() { return id; }
    public String getClassName() { return className; }

    public List<ConstructorArg> getConstructorArgs() { return constructorArgs; }
    public List<SetterArg> getSetters() { return setters; }
    public List<PropertyArg> getProperties() { return properties; }
    public List<FieldArg> getFields() { return fields; }

    // ============================
    // Classes internes
    // ============================

    public static class ConstructorArg {
        public String ref;
    }

    public static class SetterArg {
        public String method; // nom du setter
        public String ref;    // bean à injecter
    }

    public static class PropertyArg {
        public String name;   // nom de la propriété
        public String ref;    // bean à injecter
    }

    public static class FieldArg {
        public String name;   // nom du champ
        public String ref;    // bean à injecter
    }
}
