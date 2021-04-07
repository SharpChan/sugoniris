package com.sugon.iris.sugonservice.service.emailService;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendMail(String to, String subject, String content);
}
