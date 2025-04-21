package org.makarovevg.mysbs.mysbs_app.controllers;


import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.makarovevg.mysbs.mysbs_app.models.Role;
import org.makarovevg.mysbs.mysbs_app.security.PersonDetails;
import org.makarovevg.mysbs.mysbs_app.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@PostAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PersonService personService;


    @Autowired
    public AdminController(PersonService personService) {
        this.personService = personService;
    }


    @PostMapping("/create")
    public String createUser(@ModelAttribute Person newPerson, ModelMap model) {

        try {
            personService.create(newPerson);
            model.addAttribute("newPerson", newPerson); // для таймлифа
        } catch (RuntimeException e) {
            return "admin/personAlreadyCreated";
        }
        return "redirect:/admin";
    }


    @GetMapping
    public String listUsers(ModelMap model) {

        //получаем объект userDetails, который прошёл аутентификацию и авторизацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //вытаскиваем из принципал (объект успешной аутентификации, содержащий пользователя) нашего персона
        PersonDetails personAuth = (PersonDetails) authentication.getPrincipal();

        List<Person> listPersons = personService.readPersons();

        for (Person p : listPersons) {
            p.setPassword(p.getOriginalPassword());
        }

        model.addAttribute("listPersons", listPersons);
        model.addAttribute("personAuth", personAuth.getPerson());
        model.addAttribute("personInfo", personAuth.getPerson());

        return "admin/listPersons";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) long id) {

        //получаем объект userDetails, который прошёл аутентификацию и авторизацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //вытаскиваем из принципал (объект успешной аутентификации, содержащий пользователя) нашего персона
        PersonDetails personAuth = (PersonDetails) authentication.getPrincipal();

        if (personAuth.getPerson().getId() == id) {
            return "admin/personErrorDeleted";
        }
        personService.delete(id);
        return "redirect:/admin";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute Person person) {

        personService.update(person.getId(), person);
        return "redirect:/admin";
    }

}
