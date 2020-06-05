package ch.swissq.personservice.service;

import ch.swissq.personservice.model.Person;
import ch.swissq.personservice.model.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Person registerPerson(Person personToRegister) {
        return personRepository.save(personToRegister);
    }

    public Person updatePerson(long id, Person personUpdate) {
        Person existingPerson = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return personRepository.save(existingPerson.withDataFrom(personUpdate));
    }

    public Set<Person> findPersonsWithIds(Set<Long> ids) {
        return personRepository.findAllByIdIn(ids);
    }

    public boolean existsPersonWithId(long id) {
        return personRepository.existsById(id);
    }
}
