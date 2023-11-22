package com.test.record.mail.service;

import org.springframework.mail.MailException;

public interface MailService {
    
    public void sendEmail(String to, String subject, String body) throws MailException;
}
