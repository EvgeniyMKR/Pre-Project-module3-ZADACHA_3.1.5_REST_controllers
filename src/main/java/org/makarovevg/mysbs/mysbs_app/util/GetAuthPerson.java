package org.makarovevg.mysbs.mysbs_app.util;

import org.makarovevg.mysbs.mysbs_app.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class GetAuthPerson {

    public  PersonDetails getAuthPerson() {

        //получаем объект userDetails, который прошёл аутентификацию и авторизацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //вытаскиваем из принципал (объект успешной аутентификации, содержащий пользователя) нашего персона
        return  (PersonDetails) authentication.getPrincipal();
    }
}
