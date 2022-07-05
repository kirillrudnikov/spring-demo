package me.flainsky.society.service.implementation;

import lombok.AllArgsConstructor;
import me.flainsky.society.configuration.PasswordEncoder;
import me.flainsky.society.dto.SignUpForm;
import me.flainsky.society.entity.User;
import me.flainsky.society.entity.ConfirmationToken;
import me.flainsky.society.service.ConfirmationTokenService;
import me.flainsky.society.service.EmailService;
import me.flainsky.society.service.RegistrationService;
import me.flainsky.society.dto.UserDto;
import me.flainsky.society.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordEncoder passwordEncoder;

    public String register(SignUpForm form) {

        if (form.getEmail().isEmpty()) {
            throw new IllegalStateException("Адрес электронной почты не может быть пустым!");
        }

        User user = new User();
        user.setEmail(form.getEmail());
        String token = userService.createUserAccount(user);

        String link = "http://localhost:8080/registration/confirm?token=" + token;

        emailService.sendComplexMail(
                form.getEmail(),
                "noreply@society.com",
                "Подтверждение адреса электронной почты",
                emailService.getHtmlContent(form, link)
        );

        return token;
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Токен: " + token + " уже был использован");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Токен: " + token + " истёк");
        }

        confirmationTokenService.confirmTokenAt(token);

        userService.enableUserAccount(confirmationToken.getUser());
    }

    @Transactional
    public void completeRegistration(String token, UserDto form) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token);

        User userFromDb = confirmationToken.getUser();

        userFromDb.setFirstname(form.getFirstname());
        userFromDb.setLastname(form.getLastname());
        userFromDb.setUsername(form.getUsername());

        // Шифруем пароль пользователя для безопасности пользователя в случае кражи базы данных (Алгоритм BCrypt)
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(form.getPassword());
        userFromDb.setPassword(encodedPassword);

        userFromDb.setPhoneNumber(form.getPhoneNumber());

        // TODO: Было бы неплохо хранить не только пароль, но и вообще все данные БД в зашифрованном виде.
        //  Таким образом можно будет не бояться кражи базы данных вовсе, а пользователи будут в полной безопасности.

        userService.saveUser(userFromDb);
    }

}