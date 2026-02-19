package net.lyrach.metier;

import net.lyrach.dao.IDao;

public class MetierImpl implements IMetier{
    private IDao dao;

    public MetierImpl(IDao dao){
        this.dao = dao;
    }

    public MetierImpl(){};

    public void setDao(IDao dao){
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double data=dao.getValue();
        return data*67*Math.PI;
    }
}
