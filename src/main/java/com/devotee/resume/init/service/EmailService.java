package com.devotee.resume.init.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class EmailService {
         @Value("${spring.mail.properties.mail.smtp.from}")
        private String fromEmail;
         private  final JavaMailSender javaMailSender;
         public void sendHtmlEmail(String to,String sub,String htmlContent) throws MessagingException {
             log.info("Inside EmailService-SendHtmlEmail():{},{},{}",to,sub,htmlContent);
             MimeMessage message=javaMailSender.createMimeMessage();
             MimeMessageHelper helper =new MimeMessageHelper(message,true,"UTF-8");
             helper.setFrom(fromEmail);
             helper.setTo(to);
             helper.setSubject(sub);
             helper.setText(htmlContent,true);
             javaMailSender.send(message);

         }
}
