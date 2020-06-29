package com.sedadurmus.yenivavi.Model;

public class Yorum {

    private String yorum;
    private String gonderen;
    private String yorumUid;
    private String yorumGonderi;

    public String getYorumGonderi() {
        return yorumGonderi;
    }

    public void setYorumGonderi(String yorumGonderi) {
        this.yorumGonderi = yorumGonderi;
    }

    public String getYorumUid() {
        return yorumUid;
    }

    public void setYorumUid(String yorumUid) {
        this.yorumUid = yorumUid;
    }

    public Yorum() {
    }

    public Yorum(String yorum, String gonderen) {
        this.yorum = yorum;
        this.gonderen = gonderen;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }
}
