package me.flainsky.society.controller;

import lombok.AllArgsConstructor;
import me.flainsky.society.dto.SignUpForm;
import me.flainsky.society.dto.UserDto;
import me.flainsky.society.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Validated
@Controller
@AllArgsConstructor
@SessionAttributes(value = "token")
public class RegistrationController {

    private final RegistrationService registrationService;

    // Контроллер регистрации, сработает при POST методе HTTP на адрес localhost:8080/registration.
    // Содержит модель формы для регистрации.
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String createNewUserAccount(@ModelAttribute("form") SignUpForm form,
                                       BindingResult validationErrors,
                                       Model model
    ) {

        if (validationErrors.hasErrors()) {
            model.addAttribute("validationErrors", validationErrors);
        } else {
            registrationService.register(form);
        }

        return "redirect:/";
    }

    // Подтверждение почты - потенциальный новый пользователь получает на почту токен - уникальный ключ.
    @RequestMapping(value = "/registration/confirm", method = RequestMethod.GET)
    public String getEmailConfirmPage() {

        return "redirect:/";
    }

    @RequestMapping(value = "/registration/confirm", method = RequestMethod.GET, params = {"token"})
    public String confirmNewUserAccount(@RequestParam("token") String token, Model model) {

        model.addAttribute("token", token);

        return "redirect:/registration/complete";
    }

    // Завершение регистрации - получаем страницу завершения регистрации.
    @RequestMapping(value = "/registration/complete", method = RequestMethod.GET)
    public String getRegistrationCompletePage(@ModelAttribute("token") String token, Model model) {
        model.addAttribute("form", new UserDto());

        return "complete";
    }

    // Завершение регистрации - заполняем имя, фамилию, имя пользователя, телефон (Выборочно) и отправляем запрос.
    @RequestMapping(value = "/registration/complete", method = RequestMethod.POST)
    public String completeRegistration(@ModelAttribute("token") String token,
                                       @ModelAttribute("form") @Valid UserDto form,
                                       BindingResult validationErrors,
                                       SessionStatus sessionStatus
    ) {

        registrationService.completeRegistration(token, form);

        registrationService.confirmToken(token);

        sessionStatus.isComplete();


        return "redirect:/login";
    }

}