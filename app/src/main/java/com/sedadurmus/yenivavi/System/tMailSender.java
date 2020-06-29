package com.sedadurmus.yenivavi.System;

import android.util.Log;

import java.io.File;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class tMailSender {

    final static String emailPort = "587";// gmail's smtp port
    final static String smtpAuth = "true";
    final static String starttls = "true";
    final static String emailHost = "smtp.gmail.com";
    public static void send(File AFile, String AttachmentName, String subject, String bodyMessage, String... SendMails) {
         Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.ssl.trust", "smtp.google.com");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host","smtp.google.com");
//        props.put("mail.smtp.port",465);

        props = System.getProperties();
        props.put("mail.smtp.port", emailPort);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("vaveylayardim@gmail.com", "sedat4825");
                    }
                });



        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("vaveylayardim@gmail.com"));
            Address[] address = new Address[SendMails.length];//{new InternetAddress("")}

            for (int i = 0; i < SendMails.length; i++) {
                address[i] = new InternetAddress(SendMails[i]);
            }

            message.setRecipients(Message.RecipientType.BCC, address);

            message.setSubject(subject);
            message.setText(bodyMessage);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();

            try {

            } catch (Exception ex) {

            }

            messageBodyPart = new MimeBodyPart();


            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(bodyMessage);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
