package me.flainsky.society.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// Объект для передачи данных - Пользователь
@Data
public class UserDto {

    @NotBlank(message = "Имя не может быть пустым!")
    @Size(min = 2, message = "Минимальный размер: 2 символа")
    private String firstname;

    @NotBlank(message = "Фамилия не может быть пустой!")
    @Size(min = 2, message = "Минимальный размер: 2 символа")
    private String lastname;

    @NotBlank(message = "Имя пользователя не может быть пустым!")
    private String username;

    @Nullable
    private String phoneNumber;

    @NotBlank(message = "Пароль не может быть пустым!")
    @Size(min = 6, max = 64, message = "Минимальная длина пароля: 6 символов")
    private String password;

    @NotBlank(message = "Пароль не может быть пустым!")
    @Size(min = 6, max = 64, message = "Минимальная длина пароля: 6 символов")
    private String repeatPassword;

    private String profilePhoto;

}