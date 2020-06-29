package com.sedadurmus.yenivavi.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat {

    private String sender;
    private String recevier;
    private String message;
    private String messageTarihi;
    private boolean isseen;

    public Chat() {
    }

    public Chat(String sender, String recevier, String message, String messageTarihi, boolean isseen) {
        this.sender = sender;
        this.recevier = recevier;
        this.message = message;
        this.messageTarihi = messageTarihi;
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecevier() {
        return recevier;
    }

    public void setRecevier(String recevier) {
        this.recevier = recevier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Date getTarih() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dtt = null;
        try {
            if (messageTarihi != null)
                dtt = df.parse(messageTarihi);

            else return  new Date();
        } catch (ParseException e) {

        }

        return dtt;
    }

    public String getMessageTarihi() {
        return messageTarihi;
    }

    public void setMessageTarihi(String messageTarihi) {
        this.messageTarihi = messageTarihi;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
