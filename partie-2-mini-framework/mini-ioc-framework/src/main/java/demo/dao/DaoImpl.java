package demo.dao;

import net.lyrach.annotation.Component;

@Component("dao")
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        System.out.println("Version base de données");
        return 54;
    }
}
