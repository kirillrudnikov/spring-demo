package me.flainsky.society.service.implementation;

import lombok.AllArgsConstructor;
import me.flainsky.society.exception.NotFoundException;
import me.flainsky.society.entity.ConfirmationToken;
import me.flainsky.society.repository.ConfirmationTokenRepository;
import me.flainsky.society.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Token: " + token + " not found"));
    }

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public void confirmTokenAt(String token) {
        ConfirmationToken tokenFromDB = getConfirmationToken(token);
        tokenFromDB.setConfirmedAt(LocalDateTime.now());
        saveConfirmationToken(tokenFromDB);
    }

}