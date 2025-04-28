package org.makarovevg.mysbs.mysbs_app.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.makarovevg.mysbs.mysbs_app.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonDaoImpl implements PersonDao {

    @PersistenceContext
    private EntityManager entityManager;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public PersonDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void create(Person person) {

        Person newPerson = entityManager.createQuery("from Person u where  u.personName =:PersonName", Person.class)
                .setParameter("PersonName", person.getPersonName())
                .getResultStream()
                .findAny()
                .orElse(null);

        if (newPerson == null) {
            person.setOriginalPassword(person.getPassword());

            person.setPassword(passwordEncoder.encode(person.getPassword()));
            entityManager.persist(person);
        } else {
            throw new RuntimeException("Пользователь \"" + person.getPersonName() + "\" уже существует, поэтому не был создан!");
        }
    }

    @Override
    public List<Person> readPersons() {

        return entityManager.createQuery("from Person", Person.class)
                .getResultList();
    }

    @Override
    public void update(long id, Person person) {

        entityManager.createQuery("update Person u set  u.personName = :personName, u.password = :password, u.originalPassword=:originalPassword where u.id = :id")
                .setParameter("personName", person.getPersonName())
                .setParameter("password", passwordEncoder.encode(person.getPassword()))
                .setParameter("originalPassword", person.getOriginalPassword())
                .setParameter("id", person.getId())
                .executeUpdate();
    }

    @Override
    public void delete(long id) {

        entityManager.createQuery("delete from Person u where u.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Optional<Person> findByPersonId(long id) {

        return Optional.of(entityManager.createQuery("from Person u where u.id = :id", Person.class)
                .setParameter("id", id)
                .getSingleResult());

    }

    @Override
//    @EntityGraph(attributePaths = "roles") // Явно указываем загрузить роли  этом методе для Security!! УКАЗАЛИ ЯВНО в запросе, для одного случая
    public Optional<Person> findByPersonName(String personName) {

        return Optional.of(entityManager.createQuery("from Person u LEFT JOIN FETCH u.roles where u.personName = :personName", Person.class)
                .setParameter("personName", personName)
                .getSingleResult());

    }
}
