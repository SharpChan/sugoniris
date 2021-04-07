package com.sugon.iris.sugonservice.impl.emailServiceImpl;

import com.sugon.iris.sugonservice.service.emailService.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LogManager.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String sender;

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String to, String subject, String content) {
        LOGGER.debug(sender);
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送简单邮件失败");
        }
    }
}
