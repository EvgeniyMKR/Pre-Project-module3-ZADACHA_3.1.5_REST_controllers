package org.makarovevg.mysbs.mysbs_app.dao;

import org.makarovevg.mysbs.mysbs_app.models.Person;

import java.util.List;
import java.util.Optional;

public interface PersonDao {

    void create(Person person);

    List<Person> readPersons();

    void update(long id, Person person);

    void delete(long id);

    Optional<Person> findByPersonId(long id);

    Optional<Person> findByPersonName(String personName);
}
