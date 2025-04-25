package org.makarovevg.mysbs.mysbs_app.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.makarovevg.mysbs.mysbs_app.models.Role;

import java.util.Set;

@Data
@NoArgsConstructor
public class PersonDTO {

    private long id;

    private String personName;

    private String password;

    private Set<Role> roles;

    public PersonDTO(long id, String personName, String password, Set<Role> roles) {
        this.id = id;
        this.personName = personName;
        this.password = password;
        this.roles = roles;
    }

}
