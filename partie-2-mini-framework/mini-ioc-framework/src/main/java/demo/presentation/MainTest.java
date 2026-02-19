package demo.presentation;
import demo.metier.IMetier;
import net.lyrach.core.BeanDefinition;
import net.lyrach.core.SimpleApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class MainTest {
    public static void main(String[] args) {

        SimpleApplicationContext ctx = new SimpleApplicationContext();

        // Définition du bean dao
        BeanDefinition daoDef = new BeanDefinition("dao", "demo.dao.DaoImpl");

        // Définition du bean metier
        BeanDefinition metierDef = new BeanDefinition("metier", "demo.metier.MetierImpl");
        BeanDefinition.PropertyArg prop = new BeanDefinition.PropertyArg();
        prop.name = "dao";
        prop.ref = "dao";
        metierDef.getProperties().add(prop);

        // Charger les définitions
        Map<String, BeanDefinition> defs = new HashMap<>();
        defs.put("dao", daoDef);
        defs.put("metier", metierDef);

        ctx.loadBeanDefinitions(defs);

        // Récupérer le bean metier
        IMetier metier = (IMetier) ctx.getBean("metier");

        System.out.println("Résultat = " + metier.calcul());
    }
}
