package com.sedadurmus.yenivavi.notifications;

public class Data {

    private String user;
    private int icon;
    private String title;
    private String body;
    private String sented;

    public Data() {
    }

    public Data(String user, int icon, String title, String body, String sented) {
        this.user = user;
        this.icon = icon;
        this.title = title;
        this.body = body;
        this.sented = sented;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}

//    private String user,body, title, sent;
//    private Integer icon;
//
//    public Data(String uid, int ic_launcher, String title, String yeni_mesaj, String profileId) {
//    }
//
//    public Data(String user, String body, String title, String sent, Integer icon) {
//        this.user = user;
//        this.body = body;
//        this.title = title;
//        this.sent = sent;
//        this.icon = icon;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    public void setUser(String user) {
//        this.user = user;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getSent() {
//        return sent;
//    }
//
//    public void setSent(String sent) {
//        this.sent = sent;
//    }
//
//    public Integer getIcon() {
//        return icon;
//    }
//
//    public void setIcon(Integer icon) {
//        this.icon = icon;
//    }
//}
