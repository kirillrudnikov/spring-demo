package me.flainsky.society.controller;

import me.flainsky.society.dto.SignUpForm;
import me.flainsky.society.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class MainController {

    // Получаем главную страницу сайта
    // В модели содержится форма для регистрации, а также список с ошибками валидации форм
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndexPage(@Valid SignUpForm form, BindingResult validationErrors) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("form", form);
        modelAndView.addObject("errors", validationErrors);

        return modelAndView;
    }

    // Получаем страницу стены пользователя, в качестве модели передаём имя пользователя = Имя + Фамилия
    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String getFeedPage(@AuthenticationPrincipal User user, Model model) {
        String username = String.format("%s %s", user.getFirstname(), user.getLastname());
        model.addAttribute("user", username);

        return "feed";
    }

}