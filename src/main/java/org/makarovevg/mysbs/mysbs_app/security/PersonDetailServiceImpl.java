package org.makarovevg.mysbs.mysbs_app.security;

import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.makarovevg.mysbs.mysbs_app.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonDetailServiceImpl implements UserDetailsService {

    // наш сервис, через слой! не через ДАО напрямую
    private final PersonService personService;

    @Autowired
    public PersonDetailServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    //  в этом методе мы загружаем из БД нашего Person по имени, если таковой есть то возвращаем, если нет исключение
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Person> userDataBase = personService.findByPersonName(username); // достаём из БД пользователя по имени + ЕГО РОЛИ Использует @EntityGraph
        return new PersonDetails(userDataBase.orElseThrow(() ->
                new UsernameNotFoundException("Пользователь с именем \"" + username + "\" не найден!")));  // исключение ловит спринг и показывает на странице логина

    }
}
