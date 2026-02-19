package net.lyrach.presentation;

import net.lyrach.dao.IDao;
import net.lyrach.metier.IMetier;

import java.io.File;
import java.util.Scanner;

public class PresInstantiationDynamique {

    public static void main(String[] args) throws Exception {

        Scanner sc=new Scanner(new File("config.txt"));

        String daoClassName=sc.nextLine();
        String metierClassName=sc.nextLine();

        Class<?> cDao=Class.forName(daoClassName);
        Class<?> cMetier=Class.forName(metierClassName);

        IDao dao=(IDao) cDao.getConstructor().newInstance();
        IMetier metier=(IMetier) cMetier.getConstructor(IDao.class).newInstance(dao);

        System.out.println("Res="+metier.calcul());

    }
}
