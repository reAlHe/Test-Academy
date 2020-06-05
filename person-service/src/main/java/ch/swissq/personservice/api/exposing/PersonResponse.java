package ch.swissq.personservice.api.exposing;

import ch.swissq.personservice.model.Person;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponse {

    private long id;

    private String name;

    private String company;

    private String telephone;

    public static PersonResponse fromPerson(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .company(person.getCompany())
                .telephone(person.getTelephone())
                .build();
    }
}
