package org.makarovevg.mysbs.mysbs_app.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.*;

@Entity
@Table(name = "role", schema = "my_crud_schema")
@Getter
@Setter
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<Person> persons = new HashSet<>();

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public void setPerson(Person... persons) {
        Collections.addAll(this.persons, persons);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(this.roleName, role.getRoleName())
               && Objects.equals(this.persons, role.getPersons());
    }

    @Override
    public int hashCode() {
        return 27 + (this.roleName.length() << 4);
    }

    @Override
    public String toString() {
        return roleName.substring(5);
    }

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
