package me.flainsky.society.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

// Объект для передачи данных - Форма регистрации
@Data
public class SignUpForm {

    @Email(message = "Некорректный адрес электронной почты!")
    @NotBlank(message = "Адрес электронной почты не может быть пустым!")
    private String email;

}