package ch.swissq.personservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    private Long id;

    @NotBlank
    private String name;

    private String company;

    private String telephone;

    public Person withDataFrom(Person person) {
        return new Person(id, person.name, person.company, person.telephone);
    }
}
