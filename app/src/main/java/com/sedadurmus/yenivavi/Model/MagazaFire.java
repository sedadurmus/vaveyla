package com.sedadurmus.yenivavi.Model;

public class MagazaFire {
    private String magazaId;
    private String magazaResmi;
    private String magazaBasligi;
    private String magazaHakkinda;
    double magazaPuani;
    private boolean magazaDurumu;
    private double magazaAdet;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public MagazaFire() {
    }

    public MagazaFire(String magazaId, String magazaResmi, String magazaBasligi, String magazaHakkinda, double magazaPuani, boolean magazaDurumu, double magazaAdet) {
        this.magazaId = magazaId;
        this.magazaResmi = magazaResmi;
        this.magazaBasligi = magazaBasligi;
        this.magazaHakkinda = magazaHakkinda;
        this.magazaPuani = magazaPuani;
        this.magazaDurumu = magazaDurumu;
        this.magazaAdet = magazaAdet;
    }

    public String getMagazaId() {
        return magazaId;
    }

    public void setMagazaId(String magazaId) {
        this.magazaId = magazaId;
    }

    public String getMagazaResmi() {
        return magazaResmi;
    }

    public void setMagazaResmi(String magazaResmi) {
        this.magazaResmi = magazaResmi;
    }

    public String getMagazaBasligi() {
        return magazaBasligi;
    }

    public void setMagazaBasligi(String magazaBasligi) {
        this.magazaBasligi = magazaBasligi;
    }

    public String getMagazaHakkinda() {
        return magazaHakkinda;
    }

    public void setMagazaHakkinda(String magazaHakkinda) {
        this.magazaHakkinda = magazaHakkinda;
    }

    public double getMagazaPuani() {
        return magazaPuani;
    }

    public void setMagazaPuani(double magazaPuani) {
        this.magazaPuani = magazaPuani;
    }

    public boolean isMagazaDurumu() {
        return magazaDurumu;
    }

    public void setMagazaDurumu(boolean magazaDurumu) {
        this.magazaDurumu = magazaDurumu;
    }

    public double getMagazaAdet() {
        return magazaAdet;
    }

    public void setMagazaAdet(double magazaAdet) {
        this.magazaAdet = magazaAdet;
    }
}

