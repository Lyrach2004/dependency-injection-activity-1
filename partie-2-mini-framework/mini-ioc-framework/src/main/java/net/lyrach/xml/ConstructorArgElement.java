package net.lyrach.xml;

import jakarta.xml.bind.annotation.XmlAttribute;

public class ConstructorArgElement {

    private String ref;

    @XmlAttribute(name = "ref")
    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }
}
