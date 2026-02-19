package net.lyrach.ext;

import net.lyrach.dao.IDao;
import org.springframework.stereotype.Component;

@Component("d2")
public class DaoImplV2 implements IDao {


    @Override
    public double getValue() {
        System.out.println("Version capteur...");
        return 45;
    }
}
