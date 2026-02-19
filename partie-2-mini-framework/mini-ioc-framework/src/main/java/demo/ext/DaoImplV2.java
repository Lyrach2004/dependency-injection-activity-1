package demo.ext;

import demo.dao.IDao;
import net.lyrach.annotation.Component;

@Component("daoCpt")
public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version capteur...");
        return 67;
    }
}
