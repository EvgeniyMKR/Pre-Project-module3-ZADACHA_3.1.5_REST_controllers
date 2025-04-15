package org.makarovevg.mysbs.mysbs_app.util;

import jakarta.annotation.PostConstruct;
import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.makarovevg.mysbs.mysbs_app.models.Role;
import org.makarovevg.mysbs.mysbs_app.service.PersonService;
import org.makarovevg.mysbs.mysbs_app.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegistrationPersonInDataBase {


    private final PersonService personService;
    private final RoleService roleService;

    @Autowired
    public RegistrationPersonInDataBase(PersonService personService,
                                        RoleService roleService) {
        this.personService = personService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void registerPersonInDataBase() {

        Role USER = new Role("ROLE_USER");
        Role ADMIN = new Role("ROLE_ADMIN");

        roleService.create(USER);
        roleService.create(ADMIN);

        Person user = new Person("user", "user", USER);
        Person admin = new Person("admin", "admin", ADMIN);

        personService.create(user);
        personService.create(admin);

    }

}
