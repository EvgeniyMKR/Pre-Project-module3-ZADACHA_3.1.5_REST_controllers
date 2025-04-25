package org.makarovevg.mysbs.mysbs_app.controllers_rest;


import jakarta.servlet.http.HttpServletRequest;
import org.makarovevg.mysbs.mysbs_app.dto.ConverterPersonDTO;
import org.makarovevg.mysbs.mysbs_app.dto.PersonDTO;
import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.makarovevg.mysbs.mysbs_app.util.GetAuthPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@PostAuthorize("hasAnyRole('USER','ADMIN')")
public class AuthPersonApiController {


    private final GetAuthPerson authPerson;
    private final ConverterPersonDTO converterPersonDTO;

    @Autowired
    public AuthPersonApiController(GetAuthPerson authPerson, ConverterPersonDTO converterPersonDTO) {
        this.authPerson = authPerson;
        this.converterPersonDTO = converterPersonDTO;
    }

    // Получение информации о текущем аутентифицированном пользователе
    @GetMapping("/auth_person")
    public PersonDTO getCurrentPerson() {

        Person person =  this.authPerson.getAuthPerson().getPerson();
        person.setPassword(person.getOriginalPassword()); // Только чтобы отобразить пароль в исходном виде)

       return converterPersonDTO.convertToPersonDTO(person);
    }

    // Выход из системы
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        try {
            // Инвалидация сессии
            request.getSession().invalidate();

            // Очистка контекста безопасности
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}





