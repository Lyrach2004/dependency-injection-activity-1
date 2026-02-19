package net.lyrach.presentation;

import net.lyrach.dao.DaoImpl;
import net.lyrach.metier.MetierImpl;

public class PresInstantiationStatique {

    public static void main(String [] args){
        DaoImpl dao=new DaoImpl();
        MetierImpl metier=new MetierImpl(dao);
        System.out.println("Res="+metier.calcul());
    }
}
