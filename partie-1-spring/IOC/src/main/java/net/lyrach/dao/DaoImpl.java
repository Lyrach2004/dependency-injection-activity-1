package net.lyrach.dao;

import org.springframework.stereotype.Component;

@Component
public class DaoImpl implements IDao{

    @Override
    public double getValue() {
        System.out.println("Version base de données");
        return 95;
    }
}
