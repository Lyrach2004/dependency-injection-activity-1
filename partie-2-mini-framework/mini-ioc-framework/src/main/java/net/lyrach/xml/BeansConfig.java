package net.lyrach.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "beans")
public class BeansConfig {

    private List<BeanElement> beans;

    @XmlElement(name = "bean")
    public List<BeanElement> getBeans() {
        return beans;
    }

    public void setBeans(List<BeanElement> beans) {
        this.beans = beans;
    }
}
