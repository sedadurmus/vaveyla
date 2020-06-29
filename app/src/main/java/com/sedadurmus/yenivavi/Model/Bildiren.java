package com.sedadurmus.yenivavi.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bildiren {
    private String kullaniciid;
    private String text;
    private String gonderiid;
    private String bildirimTarihi;
    private boolean ispost;

    public Bildiren() {
    }

    public Bildiren(String kullaniciid, String text, String gonderiid, String bildirimTarihi, boolean ispost) {
        this.kullaniciid = kullaniciid;
        this.text = text;
        this.gonderiid = gonderiid;
        this.bildirimTarihi = bildirimTarihi;
        this.ispost = ispost;
    }

    public String getKullaniciid() {
        return kullaniciid;
    }

    public void setKullaniciid(String kullaniciid) {
        this.kullaniciid = kullaniciid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGonderiid() {
        return gonderiid;
    }

    public void setGonderiid(String gonderiid) {
        this.gonderiid = gonderiid;
    }

    public Date getTarih() {
         DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dtt = null;
        try {
            if (bildirimTarihi != null)
                dtt = df.parse(bildirimTarihi);

            else return  new Date();
        } catch (ParseException e) {

        }

        return dtt;
    }
    public void setTarih(String tarih) {
        this.bildirimTarihi = tarih;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
