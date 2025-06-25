/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class Email {
    private final String FROM;
    private final String APP_PASSWORD;
    Colors color = new Colors();

    public Email(String from, String appPassword) {
        this.FROM = from;
        this.APP_PASSWORD = appPassword;
    }

    public boolean sendVerificationCode(String to, String code) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Your Verification Code");
            message.setText("Hello!\n\nYour verification code is: " + code + "\n\nHave a nice day! ☀️");

            Transport.send(message);
            System.out.println(color.GREEN + "Verification code sent successfully to " + to + color.RESET);
            return true;
        } catch (MessagingException e) {
            System.out.println(color.RED + "Failed to send email: " + e.getMessage() + color.RESET);
            return false;
        }
    }
}

