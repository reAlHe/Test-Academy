package ch.swissq.testacademy.courseservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {

    private Long id;

    private String name;

    private String company;

    private String telephone;
}
