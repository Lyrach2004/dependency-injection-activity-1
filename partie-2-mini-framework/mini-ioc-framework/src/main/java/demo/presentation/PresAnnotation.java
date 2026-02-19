package demo.presentation;

import demo.metier.IMetier;
import net.lyrach.core.SimpleApplicationContext;

public class PresAnnotation {

    public static void main(String[] args) {
        SimpleApplicationContext ctx = new SimpleApplicationContext();
        ctx.loadAnnotatedBeans("demo");

        IMetier metier = (IMetier) ctx.getBean("metier");
        System.out.println(metier.calcul());

    }
}
