package me.flainsky.society.configuration;

import lombok.AllArgsConstructor;
import me.flainsky.society.service.implementation.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // Разрешаем доступ только к некоторым ресурсам для неавторизированных пользователей
                    .antMatchers("/", "/test", "/favicon.png", "/assets/**", "/registration/**").permitAll()
                // Остальное только для авторизированных
                    .antMatchers("/**").authenticated()
                .anyRequest()
                .authenticated()
                    .and()
                // Авторизация будет проходить через форму логина
                .formLogin()
                // Ссылка на авторизацию
                .loginProcessingUrl("/login")
                // При успешной авторизации, перекидывать на стену пользователя
                .defaultSuccessUrl("/feed", true);
    }

    // Настройка аутентификации
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    // Настройка аутентификации - выбираем шифровальщик пароля, а так же наш сервис обработки пользователей
    // Возвращаем модифицированный оператор аутентификации
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        authenticationProvider.setUserDetailsService(userService);

        return authenticationProvider;
    }

}