package org.makarovevg.mysbs.mysbs_app.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // данный метод возвращает Публичное представление для всех пользователей, главная публичная страница приложения
    // регистрируем статичные страницы без контроллеров
    public void addViewControllers(ViewControllerRegistry registry) {
        registry
                //тут условный контроллер БЕЗ ЛОГИКИ, который просто возвращает представление для всех, пришедших на сайт
                .addViewController("/")
                .setViewName("publicPage");

        registry
                .addViewController("/admin")
                .setViewName("admin/adminPage");

        registry
                .addViewController("/user")
                .setViewName("user/userPage");
    }
}
