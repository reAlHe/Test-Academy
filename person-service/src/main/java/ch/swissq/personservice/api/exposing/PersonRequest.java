package ch.swissq.personservice.api.exposing;

import ch.swissq.personservice.model.Person;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PersonRequest {

    @NotBlank
    private String name;

    private String company;

    private String telephone;

    public Person asPerson() {
        return new Person(null, name, company, telephone);
    }
}
