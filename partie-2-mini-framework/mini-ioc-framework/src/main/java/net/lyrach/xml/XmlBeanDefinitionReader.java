package net.lyrach.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import net.lyrach.core.BeanDefinition;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlBeanDefinitionReader {

    public Map<String, BeanDefinition> loadBeanDefinitions(String xmlPath) {
        try {
            JAXBContext context = JAXBContext.newInstance(BeansConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            BeansConfig config = (BeansConfig) unmarshaller.unmarshal(new File(xmlPath));

            Map<String, BeanDefinition> defs = new HashMap<>();

            for (BeanElement bean : config.getBeans()) {
                BeanDefinition def = new BeanDefinition(bean.getId(), bean.getClassName());

                if (bean.getProperties() != null) {
                    for (var prop : bean.getProperties()) {
                        BeanDefinition.PropertyArg p = new BeanDefinition.PropertyArg();
                        p.name = prop.getName();
                        p.ref = prop.getRef();
                        def.getProperties().add(p);
                    }
                }

                defs.put(def.getId(), def);
            }

            return defs;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture XML", e);
        }
    }
}
