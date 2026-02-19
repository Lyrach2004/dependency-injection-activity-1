package net.lyrach.core;

import java.util.ArrayList;
import java.util.List;

public class BeanDefinition {

    private String id;
    private String className;

    // Injection par constructeur
    private List<ConstructorArg> constructorArgs = new ArrayList<>();

    // Injection par setter
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
    public List<PropertyArg> getProperties() { return properties; }
    public List<FieldArg> getFields() { return fields; }

    public static class ConstructorArg {
        public String ref;
    }

    public static class PropertyArg {
        public String name;
        public String ref;
    }

    public static class FieldArg {
        public String name;
        public String ref;
    }
}
