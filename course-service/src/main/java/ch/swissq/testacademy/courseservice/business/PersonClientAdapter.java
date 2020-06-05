package ch.swissq.testacademy.courseservice.business;

import ch.swissq.testacademy.courseservice.model.Person;

import java.util.Set;

public interface PersonClientAdapter {

    boolean isPersonWithIdExistent(long id);

    Set<Person> getPersonInformationForIds(Set<Long> ids);
}
