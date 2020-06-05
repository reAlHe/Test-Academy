package ch.swissq.testacademy.courseservice.api.consuming;

import ch.swissq.testacademy.courseservice.business.PersonClientAdapter;
import ch.swissq.testacademy.courseservice.model.Person;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Component
@Slf4j
public class PersonClientAdapterImpl implements PersonClientAdapter {

    private final PersonClient personClient;

    @Override
    public boolean isPersonWithIdExistent(long id) {
        try {
            return personClient.retrievePersonIdExists(id).getStatusCode().is2xxSuccessful();
        }
        catch (FeignException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Set<Person> getPersonInformationForIds(Set<Long> ids) {
        try {
            return new HashSet<>(personClient.retrievePersonInformationForIds(ids).getBody());
        }
        catch (FeignException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
