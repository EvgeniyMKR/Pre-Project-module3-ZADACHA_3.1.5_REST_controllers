package org.makarovevg.mysbs.mysbs_app.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;


import java.util.*;

@Entity
@Table(name = "person", schema = "my_crud_schema")
@Getter
@Setter
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "person_name", nullable = false)
    private String personName;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "original_password")
    private String originalPassword;

    @ManyToMany(fetch = FetchType.LAZY)  // Ленивая загрузка этой СУЩНОСТи по умолчанию
    @Fetch(FetchMode.JOIN)  // Загружает Сущность  JOIN-запросом в одной транзакции при Lazy!
    @JoinTable(
            name = "person_role", // связующая таблица, указываем явно для читабельности
            joinColumns = @JoinColumn(name = "person_id"), // внешний ключ со стороны Person
            inverseJoinColumns = @JoinColumn(name = "role_id") // внешний ключ со стороны Role
    )

    private Set<Role> roles = new HashSet<>();


    public Person(String personName, String password, Role... roles) {
        this.personName = personName;
        this.password = password;
        Collections.addAll(this.roles, roles);
    }

    public void setRolesByRoleName(Role... roles) {
        this.roles.clear();
        Collections.addAll(this.roles, roles);
    }

    public String getOriginalPassword() {
        return this.originalPassword.substring(0, 2) + "*****";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(this.personName, person.getPersonName())
               && Objects.equals(this.password, person.getPassword())
               && Objects.equals(this.roles, person.getRoles());
    }

    @Override
    public int hashCode() {
        return 7 + (this.personName.length() << 2)
               + (this.password.length() << 3) + (this.roles.size());
    }

    @Override
    public String toString() {
        return "Person{" +
               "personName='" + personName + '\'' +
               ", password='" + password + '\'' +
               ", roles=" + roles +
               '}';
    }
}
