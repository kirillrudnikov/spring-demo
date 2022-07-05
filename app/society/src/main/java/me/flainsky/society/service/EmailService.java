package me.flainsky.society.service;

import me.flainsky.society.dto.SignUpForm;

public interface EmailService {

    String getHtmlContent(SignUpForm form, String link);

    void sendSimpleMail(String to, String from, String subject, String text);

    void sendComplexMail(String to, String from, String subject, String text);

}