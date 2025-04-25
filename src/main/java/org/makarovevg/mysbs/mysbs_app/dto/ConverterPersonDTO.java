package org.makarovevg.mysbs.mysbs_app.dto;

import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConverterPersonDTO {


    public PersonDTO convertToPersonDTO(Person person) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(person, PersonDTO.class);  // ИЗ person --> в PersonDTO
    }

    public Person convertToPerson(PersonDTO personDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(personDTO, Person.class); // ИЗ PersonDTO --> в Person

    }
}
