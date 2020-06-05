package ch.swissq.personservice.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Set<Person> findAllByIdIn(Set<Long> ids);
}
