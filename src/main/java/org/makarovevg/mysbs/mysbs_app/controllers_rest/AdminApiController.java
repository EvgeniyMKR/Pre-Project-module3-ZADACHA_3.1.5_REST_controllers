package org.makarovevg.mysbs.mysbs_app.controllers_rest;


import org.makarovevg.mysbs.mysbs_app.dto.ConverterPersonDTO;
import org.makarovevg.mysbs.mysbs_app.dto.PersonDTO;
import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.makarovevg.mysbs.mysbs_app.service.PersonService;
import org.makarovevg.mysbs.mysbs_app.util.GetAuthPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@PostAuthorize("hasRole('ADMIN')")
public class AdminApiController {

    private final PersonService personService;
    private final GetAuthPerson authPerson;
    private final ConverterPersonDTO converterPersonDTO;


    @Autowired
    public AdminApiController(PersonService personService, GetAuthPerson authPerson, ConverterPersonDTO converterPersonDTO) {
        this.personService = personService;
        this.authPerson = authPerson;
        this.converterPersonDTO = converterPersonDTO;
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED) //201
    public void createUser(@RequestBody PersonDTO newPersonDTO) {

        try {
            Person newPerson = converterPersonDTO.convertToPerson(newPersonDTO);
            personService.create(newPerson);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @GetMapping("/persons")
    public List<PersonDTO> readPersons() {

        return personService.readPersons().stream()
                .peek((Person person) -> {
                    person.setPassword(person.getOriginalPassword());
                })
                .map(converterPersonDTO::convertToPersonDTO)
                .collect(Collectors.toList());
    }


    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT) //204
    public void deleteUser(@RequestParam(value = "id", required = false) long id) {

        if (authPerson.getAuthPerson().getPerson().getId() == id) { // попытка удалить себя
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        personService.delete(id);
    }


    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT) //204
    public void updateUser(@RequestBody PersonDTO personDTO) {

        Person updatedPerson = converterPersonDTO.convertToPerson(personDTO);
        personService.update(updatedPerson.getId(), updatedPerson);
    }

}
