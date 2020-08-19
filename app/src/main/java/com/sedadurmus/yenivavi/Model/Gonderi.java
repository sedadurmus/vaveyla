package com.sedadurmus.yenivavi.Model;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gonderi {

    private String gonderiId;
    private String gonderiResmi;
    private String gonderiVideo;
    private String gonderiHakkinda;
    private String gonderiTuru;
    private String gonderen;
    private String gonderiTarihi;
    private String gonderiUid;
    private boolean gorevmi, onayDurumu;

    public String getGonderiUid() {
        return gonderiUid;
    }

    public void setGonderiUid(String gonderiUid) {
        this.gonderiUid = gonderiUid;
    }

    public Gonderi() {
    }


    public boolean isGorevmi() {
        return gorevmi;
    }

    public void setGorevmi(boolean gorevmi) {
        this.gorevmi = gorevmi;
    }

    public Gonderi(String gonderiId, String gonderiResmi, String gonderiVideo, String gonderiHakkinda, String gonderen, String gonderiTarihi, boolean gorevmi, boolean onayDurumu) {
        this.gonderiId = gonderiId;
        this.gonderiResmi = gonderiResmi;
        this.gonderiVideo= gonderiVideo;
        this.gonderiHakkinda = gonderiHakkinda;
        this.gonderen = gonderen;
        this.gonderiTarihi = gonderiTarihi;
        this.gorevmi = gorevmi;
        this.onayDurumu = onayDurumu;
    }


    public boolean isOnayDurumu() {
        return onayDurumu;
    }

    public void setOnayDurumu(boolean onayDurumu) {
        this.onayDurumu = onayDurumu;
    }

    public String getGonderiId() {
        return gonderiId;
    }

    public void setGonderiId(String gonderiId) {
        this.gonderiId = gonderiId;
    }

    public String getGonderiResmi() {
        return gonderiResmi;
    }

    public void setGonderiResmi(String gonderiResmi) {
        this.gonderiResmi = gonderiResmi;
    }

    public String getGonderiVideo() {
        return gonderiVideo;
    }

    public void setGonderiVideo(String gonderiResmi) {
        this.gonderiVideo = gonderiVideo;
    }

    public String getGonderiHakkinda() {
        return gonderiHakkinda;
    }

    public void setGonderiHakkinda(String gonderiHakkinda) {
        this.gonderiHakkinda = gonderiHakkinda;
    }
    public String getGonderiTuru() {
        return gonderiTuru;
    }

    public void setGonderiTuru(String gonderiTuru) {
        this.gonderiTuru = gonderiTuru;
    }


    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public Date getTarih() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dtt = null;
        try {
            if (gonderiTarihi != null)
                dtt = df.parse(gonderiTarihi);

            else return  new Date();
        } catch (ParseException e) {

        }

        return dtt;
    }

    public void setTarih(String tarih) {
        this.gonderiTarihi = tarih;
    }
}



