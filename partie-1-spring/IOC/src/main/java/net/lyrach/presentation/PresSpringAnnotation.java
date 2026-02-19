package net.lyrach.presentation;

import net.lyrach.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresSpringAnnotation {
    public static void main(String[] args) {

        ApplicationContext context=new AnnotationConfigApplicationContext("net.lyrach");
        IMetier metier=context.getBean(IMetier.class);
        System.out.println("Res="+metier.calcul());
    }
}
