package me.flainsky.society.service;

import me.flainsky.society.entity.ConfirmationToken;

public interface ConfirmationTokenService {

    ConfirmationToken getConfirmationToken(String token);

    void saveConfirmationToken(ConfirmationToken confirmationToken);

    void confirmTokenAt(String token);

}
