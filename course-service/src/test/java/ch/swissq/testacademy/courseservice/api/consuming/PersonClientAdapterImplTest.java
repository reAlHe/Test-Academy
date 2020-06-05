package ch.swissq.testacademy.courseservice.api.consuming;

import feign.codec.EncodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.swissq.testacademy.courseservice.builder.PersonBuilder.aPerson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

class PersonClientAdapterImplTest {

    private PersonClientAdapterImpl underTest;

    @Mock
    private PersonClient personClientMock;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        underTest = new PersonClientAdapterImpl(personClientMock);
    }

    @Test
    void retrievePersonExistenceSuccessfully() {
        var personId = 1L;
        when(personClientMock.retrievePersonIdExists(personId)).thenReturn(ResponseEntity.ok().build());

        assertThat(underTest.isPersonWithIdExistent(personId)).isTrue();
    }

    @Test
    void retrievePersonNotExists() {
        var personId = 1L;
        when(personClientMock.retrievePersonIdExists(personId)).thenReturn(ResponseEntity.notFound().build());

        assertThat(underTest.isPersonWithIdExistent(personId)).isFalse();
    }

    @Test
    void retrievePersonReturnsFalseOnError() {
        var personId = 1L;
        when(personClientMock.retrievePersonIdExists(personId)).thenThrow(new EncodeException("An error occurred"));

        assertThat(underTest.isPersonWithIdExistent(personId)).isFalse();
    }

    @Test
    void retrievePersonInformationSuccessfully() {
        var participantIds = Stream.of(1L, 2L)
                .collect(Collectors.toSet());
        var person1 = aPerson().withId(1L).withName("Silvio").withCompany("SwissQ").withTelephone("123").build();
        var person2 = aPerson().withId(2L).withName("Frank").withCompany("UBS").withTelephone("456").build();
        var participants = Arrays.asList(person1, person2);
        when(personClientMock.retrievePersonInformationForIds(participantIds)).thenReturn(ResponseEntity.ok().body(participants));

        assertThat(underTest.getPersonInformationForIds(participantIds)).containsOnly(person1, person2);
    }

    @Test
    void retrievePersonInformationFailsOnError() {
        var participantIds = Stream.of(1L, 2L)
                .collect(Collectors.toSet());
        when(personClientMock.retrievePersonInformationForIds(participantIds)).thenThrow(new EncodeException("An error occurred"));

        assertThatExceptionOfType(EncodeException.class).isThrownBy(() -> underTest.getPersonInformationForIds(participantIds));
    }
}