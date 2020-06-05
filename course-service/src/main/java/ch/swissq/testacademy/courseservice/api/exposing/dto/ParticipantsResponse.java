package ch.swissq.testacademy.courseservice.api.exposing.dto;

import ch.swissq.testacademy.courseservice.model.Person;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ParticipantsResponse {

    private Set<Person> participants;

}
