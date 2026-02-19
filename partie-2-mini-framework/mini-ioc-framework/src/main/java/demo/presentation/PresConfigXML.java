package demo.presentation;

import demo.metier.IMetier;
import net.lyrach.core.BeanDefinition;
import net.lyrach.core.SimpleApplicationContext;
import net.lyrach.xml.XmlBeanDefinitionReader;

import java.util.Map;

public class PresConfigXML {

    public static void main(String [] args){
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader();
        Map<String, BeanDefinition> defs = reader.loadBeanDefinitions("src/main/resources/config.xml");

        SimpleApplicationContext ctx = new SimpleApplicationContext();
        ctx.loadBeanDefinitions(defs);

        IMetier metier = (IMetier) ctx.getBean("metier");
        System.out.println("Résultat = " + metier.calcul());

    }
}
