package com.sfood.eai.external.service;

@FunctionalInterface
public interface MailService {
    void send(String to, String subject, String body);
}
