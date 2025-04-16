package org.makarovevg.mysbs.mysbs_app.service;


import org.makarovevg.mysbs.mysbs_app.dao.PersonDao;
import org.makarovevg.mysbs.mysbs_app.dao.RoleDao;
import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.makarovevg.mysbs.mysbs_app.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class PersonServiceImpl implements PersonService {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final PersonDao personDao;
    private final RoleDao roleDao;

    @Autowired
    public PersonServiceImpl(PersonDao personDao, RoleDao roleDao) {
        this.personDao = personDao;
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public void create(Person person) {
        Role role;
        Set<Role> roles = person.getRoles();

        try {
            if (roles.contains(new Role("ROLE_ADMIN"))) {
                role = (roleDao.findByRoleName("ROLE_ADMIN"))
                        .orElseThrow(() -> new RuntimeException("РОЛЬ ADMIN НЕ НАЙДЕНА В БАЗЕ ДАННЫХ!"));
            } else {
                role = roleDao.findByRoleName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("РОЛЬ USER НЕ НАЙДЕНА В БАЗЕ ДАННЫХ!"));
            }
            person.setRolesByRoleName(role);

            personDao.create(person);
            log.info("Пользователь с именем \"" + person.getPersonName() + "\" успешно создан..");

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    @Transactional (readOnly = true)
    public List<Person> readPersons() {
        return personDao.readPersons();
    }

    @Override
    @Transactional
    public void update(long id, Person person) {

        Set<Role> rolesUpdate = person.getRoles();
        Role roleUpdate;

        // Достаю юзера
        Person personUpdate = personDao.findByPersonId(id) // id беру именно с фронта, так как у пришедшего Person его нету!!
                .orElseThrow(() -> new RuntimeException("ПОЛЬЗОВАТЕЛЬ НЕ НАЙДЕН В БД! ИЗМЕНЕНИЕ НЕ УДАЛОСЬ!!!"));


        try {
            if (rolesUpdate.contains(new Role("ROLE_ADMIN"))) {
                roleUpdate = (roleDao.findByRoleName("ROLE_ADMIN"))
                        .orElseThrow(() -> new RuntimeException("РОЛЬ ADMIN НЕ НАЙДЕНА В БАЗЕ ДАННЫХ!"));
            } else {
                roleUpdate = roleDao.findByRoleName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("РОЛЬ USER НЕ НАЙДЕНА В БАЗЕ ДАННЫХ!"));
            }

            personUpdate.setRolesByRoleName(roleUpdate); // обновляю роль юзеру из БД

            personUpdate.setPersonName(person.getPersonName()); // обновляю имя юзеру из БД
            personUpdate.setPassword(person.getPassword()); // обновляю пароль

            personDao.update(id, personUpdate); // теперь нужно в рамках транзакции этой ОБНОВИТЬ в самой БД его данные

            log.info("Пользователь с id \"" + person.getId() + "\" успешно изменен!");

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        personDao.delete(id);
        log.info("Пользователь по id \"" + id + "\", имеющий запись в БД, был успешно удален!");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findByPersonName(String personName) {
        return personDao.findByPersonName(personName);
    }
}
