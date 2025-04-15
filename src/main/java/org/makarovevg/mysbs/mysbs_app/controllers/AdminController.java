package org.makarovevg.mysbs.mysbs_app.controllers;


import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.makarovevg.mysbs.mysbs_app.models.Role;
import org.makarovevg.mysbs.mysbs_app.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
            System.err.println(e.getMessage());
            return "admin/personAlreadyCreated";
        }
        return "admin/personCreated";
    }


    @GetMapping
    public String listUsers(ModelMap model) {
        List<Person> listPersons = personService.readPersons();
        if (listPersons.isEmpty()) {
            return "admin/emptyList";
        }
        model.addAttribute("listPersons", listPersons);

        return "admin/listPersons";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) long id, ModelMap model) {
        personService.delete(id);
        model.addAttribute("personDeleted", id);

        return "/admin/personDeleted";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute Person person, ModelMap model) {

        personService.update(person.getId(), person);
        model.addAttribute("personUpdated", person.getId());

        return "/admin/personUpdated";
    }

}
