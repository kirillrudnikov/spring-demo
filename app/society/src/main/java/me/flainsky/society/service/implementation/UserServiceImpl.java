package me.flainsky.society.service.implementation;

import lombok.AllArgsConstructor;
import me.flainsky.society.entity.User;
import me.flainsky.society.entity.UserRole;
import me.flainsky.society.exception.NotFoundException;
import me.flainsky.society.repository.UserRepository;
import me.flainsky.society.configuration.PasswordEncoder;
import me.flainsky.society.dto.UserDto;
import me.flainsky.society.exception.AlreadyExistsException;
import me.flainsky.society.entity.ConfirmationToken;
import me.flainsky.society.service.ConfirmationTokenService;
import me.flainsky.society.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;

    // Получить пользователя из базы данных по уникальному номеру
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    // Сохранить пользователя в базу данных
    public boolean saveUser(User user) {
        userRepository.save(user);

        return true;
    }

    // Создать новый аккаунт
    public String createUserAccount(User user) {

        if (isEmailAlreadyExists(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с почтой " + user.getEmail() + " уже существует");
        }

        // Устанавливаем роль пользователя = USER (обычный пользователь)
        user.setRole(UserRole.USER);

        // Аккаунт пользователя пока не забанен
        user.setIsNonLocked(true);

        // Временно отключить аккаунт пользователя (Пользователь не сможет авторизоваться пока не подтвердит почту)
        user.setIsEnabled(false);

        // Создать токен для подтверждения почты
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, user);

        // Сохраняем пользователя и его токен в БД
        userRepository.save(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // Данный метод в качестве результата вернёт тот самый токен для подтверждения почты
        return token;
    }

    // Активировать аккаунт пользователя
    public void enableUserAccount(User user) {
        user.setIsEnabled(true);
    }

    // Наш пользователь способен авторизоваться при помощи адреса электронной почты (email) или краткого имени пользователя (username)
    @Override
    public UserDetails loadUserByUsername(String credential) {
        return userRepository.findByEmailOrUsername(credential, credential)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Boolean isEmailAlreadyExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private Boolean isUsernameAlreadyExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

}