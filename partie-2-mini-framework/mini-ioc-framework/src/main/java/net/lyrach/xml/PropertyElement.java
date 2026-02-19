package net.lyrach.xml;

import jakarta.xml.bind.annotation.XmlAttribute;

public class PropertyElement {

    private String name;
    private String ref;

    @XmlAttribute(name = "name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @XmlAttribute(name = "ref")
    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }
}
