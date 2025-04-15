package org.makarovevg.mysbs.mysbs_app.controllers;


import org.makarovevg.mysbs.mysbs_app.security.PersonDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String helloWorld(ModelMap model) {

        //получаем объект userDetails, который прошёл аутентификацию и авторизацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //вытаскиваем из принципал (объект успешной аутентификации, содержащий пользователя) нашего персона
        PersonDetails person = (PersonDetails) authentication.getPrincipal();

        model.addAttribute("person", person.getPerson());

        return "user/userPage";
    }
}
