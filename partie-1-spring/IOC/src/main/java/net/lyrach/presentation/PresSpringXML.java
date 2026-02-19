package net.lyrach.presentation;

import net.lyrach.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresSpringXML {

    public static void main(String[] args) {

        ApplicationContext context=new ClassPathXmlApplicationContext("config.xml");
        IMetier metier=(IMetier) context.getBean("metier");
        System.out.println("Res="+metier.calcul());
    }
}
