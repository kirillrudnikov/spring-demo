package me.flainsky.society.service;

import me.flainsky.society.dto.SignUpForm;
import me.flainsky.society.dto.UserDto;

public interface RegistrationService {

    String register(SignUpForm form);

    void confirmToken(String token);

    void completeRegistration(String token, UserDto form);

}