package ch.swissq.testacademy.courseservice.api.exposing;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import ch.swissq.testacademy.courseservice.model.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.swissq.testacademy.courseservice.builder.CourseBuilder.aCourse;
import static ch.swissq.testacademy.courseservice.builder.PactBuilder.participants;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "person-service", hostInterface = "localhost", port = "7000")
class CourseControllerParticipantsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void init() {
        courseRepository.deleteAll();
    }

    @Pact(consumer="course-service")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .given("there are person details provided")
                .uponReceiving("a request for fetching these information")
                .path("/persons")
                .method("GET")
                .query("ids=1")
                .willRespondWith()
                .status(200)
                .body(participants())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void getCourseParticipantsSuccessfully() throws Exception {
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var participantIds = Stream.of(1L)
                .collect(Collectors.toSet());
        var course = aCourse()
                .withCapacity(10)
                .withDescription("Cool School")
                .withLecturer(1L)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .withParticipants(participantIds)
                .build();

        var savedCourse = courseRepository.save(course);

        mockMvc.perform(get("/courses/{courseId}/participants", savedCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.participants").isArray())
                .andExpect(jsonPath("$.participants", hasSize(1)))
                .andExpect(jsonPath("$.participants[0].id", is(1)))
                .andExpect(jsonPath("$.participants[0].name", is("Silvio")))
                .andExpect(jsonPath("$.participants[0].company", is("SwissQ")))
                .andExpect(jsonPath("$.participants[0].telephone", is("12345")));
    }
}