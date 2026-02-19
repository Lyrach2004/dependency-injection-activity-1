package demo.metier;

import demo.dao.IDao;
import net.lyrach.annotation.Autowired;
import net.lyrach.annotation.Component;

@Component("metier")
public class MetierImpl implements IMetier {


    private IDao dao;

    public void setDao(IDao dao){
        this.dao = dao;
    }

    @Autowired
    public MetierImpl(IDao dao){
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double data= dao.getData();
        return data*2*Math.PI;
    }
}
