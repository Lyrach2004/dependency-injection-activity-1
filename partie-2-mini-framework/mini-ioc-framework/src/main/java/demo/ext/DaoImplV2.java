package demo.ext;

import demo.dao.IDao;

public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version capteur...");
        return 67;
    }
}
