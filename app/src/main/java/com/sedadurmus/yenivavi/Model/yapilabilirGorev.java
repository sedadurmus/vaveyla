package com.sedadurmus.yenivavi.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class yapilabilirGorev {
    private String gorevId;
    private String gorevResmi;
    private String gorevBasligi;
    private String gorevHakkinda;
    double gorevPuani;
    private boolean gorevDurumu;
    private String gorevBaslangic;
    private String gorevBitis;

    public yapilabilirGorev(){}

    public yapilabilirGorev(String gorevId, String gorevResmi, String gorevBasligi, String gorevHakkinda, double gorevPuani, boolean gorevDurumu, String gorevBaslangic, String gorevBitis) {
        this.gorevId = gorevId;
        this.gorevResmi = gorevResmi;
        this.gorevBasligi = gorevBasligi;
        this.gorevHakkinda = gorevHakkinda;
        this.gorevPuani = gorevPuani;
        this.gorevDurumu = gorevDurumu;
        this.gorevBaslangic = gorevBaslangic;
        this.gorevBitis = gorevBitis;
    }

    public String getGorevId() {
        return gorevId;
    }

    public void setGorevId(String gorevId) {
        this.gorevId = gorevId;
    }

    public String getGorevResmi() {
        return gorevResmi;
    }

    public void setGorevResmi(String gorevResmi) {
        this.gorevResmi = gorevResmi;
    }

    public String getGorevBasligi() {
        return gorevBasligi;
    }

    public void setGorevBasligi(String gorevBasligi) {
        this.gorevBasligi = gorevBasligi;
    }

    public String getGorevHakkinda() {
        return gorevHakkinda;
    }

    public void setGorevHakkinda(String gorevHakkinda) {
        this.gorevHakkinda = gorevHakkinda;
    }

    public double getGorevPuani() {
        return gorevPuani;
    }

    public void setGorevPuani(double gorevPuani) {
        this.gorevPuani = gorevPuani;
    }

    public boolean isGorevDurumu() {
        return gorevDurumu;
    }

    public void setGorevDurumu(boolean gorevDurumu) {
        this.gorevDurumu = gorevDurumu;
    }

    public String getGorevBaslangic() {
        return gorevBaslangic;
    }

    public void setGorevBaslangic(String gorevBaslangic) {
        this.gorevBaslangic = gorevBaslangic;
    }

    public String getGorevBitis() {
        return gorevBitis;
    }

    public void setGorevBitis(String gorevBitis) {
        this.gorevBitis = gorevBitis;
    }

    public boolean compareDate() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateBaslangic = null;
        Date simdi=new Date();
        try {
            if (gorevBaslangic != null)
                dateBaslangic = df.parse(gorevBaslangic);

            else
                dateBaslangic = new Date();
        } catch (ParseException e) {

        }



        Date dateBitis= null;
        try {
            if (gorevBaslangic != null)
                dateBitis = df.parse(gorevBitis);

            else
                dateBitis = new Date();
        } catch (ParseException e) {

        }

        int basladimi=simdi.compareTo(dateBaslangic);
        int bittimi=simdi.compareTo(dateBitis);

        return  basladimi>0&&bittimi<0;


    }
}
