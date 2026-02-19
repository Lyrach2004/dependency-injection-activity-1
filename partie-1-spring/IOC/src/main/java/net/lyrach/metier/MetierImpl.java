package net.lyrach.metier;

import net.lyrach.dao.IDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MetierImpl implements IMetier{
    private IDao dao;

    public MetierImpl(@Qualifier("d2") IDao dao){
        this.dao = dao;
    }


    public void setDao(IDao dao){
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double data=dao.getValue();
        return data*67*Math.PI;
    }
}
