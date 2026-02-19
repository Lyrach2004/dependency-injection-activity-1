package net.lyrach.xml;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

public class BeanElement {

    private String id;
    private String className;
    private List<PropertyElement> properties;

    private List<FieldElement> fields;

    @XmlElement(name = "field")
    public List<FieldElement> getFields() { return fields; }
    public void setFields(List<FieldElement> fields) { this.fields = fields; }

    private List<ConstructorArgElement> constructorArgs;

    @XmlElement(name = "constructor-arg")
    public List<ConstructorArgElement> getConstructorArgs() { return constructorArgs; }
    public void setConstructorArgs(List<ConstructorArgElement> constructorArgs) {
        this.constructorArgs = constructorArgs;
    }


    @XmlAttribute(name = "id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @XmlAttribute(name = "class")
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    @XmlElement(name = "property")
    public List<PropertyElement> getProperties() { return properties; }
    public void setProperties(List<PropertyElement> properties) { this.properties = properties; }
}
