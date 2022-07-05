package me.flainsky.society.service;

import me.flainsky.society.dto.UserDto;
import me.flainsky.society.entity.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    boolean saveUser(User user);

    String createUserAccount(User user);

    void enableUserAccount(User user);

}