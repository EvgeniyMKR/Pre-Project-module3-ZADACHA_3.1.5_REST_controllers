package org.makarovevg.mysbs.mysbs_app.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final SuccessUserHandler successUserHandler;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth    // ТУТ НАСТРОЙКА ДОСТУПА к URLам кому и как
                        .requestMatchers("/").permitAll() //доступны всем, без аутентификации
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN") //доступны юзерам  и админам (автоматически добавляет префикс "ROLE_")
                        .requestMatchers("/admin/**").hasRole("ADMIN") //доступны админам  (автоматически добавляет префикс "ROLE_")
                        .anyRequest().authenticated() // все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form  // ТУТ НАСТРОЙКА входа дефолтная форма аутентификации
                        // .loginPage("/login") // если ипользуем кастомную форму входа
                        .successHandler(successUserHandler) // перенаправление после успешного входа через отдельный класс
                       //.defaultSuccessUrl("/user")// перенаправление после успешного входа  ИСПОЛЬЗУЕТСЯ ДЕФОЛТНАЯ!
                        .permitAll() // доступ к странице входа без аутентификации
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") //перенаправление после выхода
                        .invalidateHttpSession(true) // Очистка сессии
                        .deleteCookies("JSESSIONID") // Удаление куки
                        .permitAll() // разрешает выход без дополнительных проверок
                );

        http.csrf(AbstractHttpConfigurer::disable); // для Post запросов отключает защиту от CSRF (Cross-Site Request Forgery)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // для хэширования паролей, c кодированием
    }

//    @Bean
//    public PasswordEncoder noOpPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); //  без кодирования
//    }

    // В современном Spring Security (начиная с версии 5.7+) явное объявление бина аутентификации не требуется,
    // так как Spring Boot автоматически настраивает аутентификацию, когда обнаруживает:
    //кастомный UserDetailsService (PersonDetailService)
    //Бин PasswordEncoder
}
