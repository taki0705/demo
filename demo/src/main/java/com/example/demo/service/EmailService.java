package com.example.demo.service;

import com.example.demo.dto.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailsender;

    public EmailService(JavaMailSender javaMailsender) {
        this.javaMailsender = javaMailsender;
    }
    public void sendSimpleMessage(MailBody mailbody){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(mailbody.to());
        message.setFrom("bhh07052003@gmai.com");
        message.setSubject(mailbody.subject());
        message.setText(mailbody.text());
        javaMailsender.send(message);
    }
}
