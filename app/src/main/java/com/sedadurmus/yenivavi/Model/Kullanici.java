package com.sedadurmus.yenivavi.Model;

public class Kullanici {

    private String id;
    private String kullaniciadi;
    private String ad;
    private String resimurl;
    private String bio;
    private String mail;
    private double profilPuan;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Kullanici() {
    }


    public Kullanici(String id, String kullaniciadi, String ad, String resimurl, String bio, double profilPuan ) {
        this.id = id;
        this.kullaniciadi = kullaniciadi;
        this.ad = ad;
        this.resimurl = resimurl;
        this.bio = bio;
        this.profilPuan = profilPuan;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public double getProfilPuan() {
        return profilPuan;
    }

    public void setProfilPuan(double profilPuan) {
        this.profilPuan = profilPuan;
    }

}
