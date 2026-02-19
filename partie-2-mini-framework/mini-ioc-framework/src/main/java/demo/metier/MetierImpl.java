package demo.metier;

import demo.dao.IDao;

public class MetierImpl implements IMetier {

    private IDao dao;

    public void setDao(IDao dao){
        this.dao = dao;
    }

    public MetierImpl(IDao dao){
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double data= dao.getData();
        return data*2*Math.PI;
    }
}
