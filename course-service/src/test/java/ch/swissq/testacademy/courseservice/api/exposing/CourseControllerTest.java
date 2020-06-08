package ch.swissq.testacademy.courseservice.api.exposing;

import ch.swissq.testacademy.courseservice.api.consuming.PersonClientAdapterImpl;
import ch.swissq.testacademy.courseservice.model.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.swissq.testacademy.courseservice.builder.CourseBuilder.aCourse;
import static ch.swissq.testacademy.courseservice.builder.CourseRequestBuilder.aCourseRequest;
import static ch.swissq.testacademy.courseservice.builder.PersonBuilder.aPerson;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseControllerTest {

    @MockBean
    private PersonClientAdapterImpl personClientAdapterImplMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void init() {
        courseRepository.deleteAll();
    }

    @Test
    void registerCourseSuccessfully() throws Exception {
        var start = LocalDateTime.of(2020, 10, 10, 8, 0);
        var end = start.plusHours(8);
        var courseRequest = aCourseRequest()
                .withName("Test Academy")
                .withCapacity(10)
                .withDescription("Cool School")
                .withLecturer(1L)
                .withStart(start)
                .withEnd(end)
                .build();

        when(personClientAdapterImplMock.isPersonWithIdExistent(1L)).thenReturn(true);

        mockMvc.perform(post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(courseRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name", is("Test Academy")))
                .andExpect(jsonPath("$.description", is("Cool School")))
                .andExpect(jsonPath("$.start", is(start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.capacity", is(10)))
                .andExpect(jsonPath("$.lecturer", is(1)));
    }

    @Test
    void registerCourseFailsForUnknownLecturer() throws Exception {
        //TODO 8) implement test
    }

    @Test
    void registerCourseFailsForOccupiedLecturer() throws Exception {
        //TODO 9) implement test
    }

    @Test
    void updateCourseSuccessfully() throws Exception {
        var start = LocalDateTime.of(2020, 10, 10, 8, 0);
        var end = start.plusHours(8);
        var course = aCourse()
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(1L)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .build();

        var savedCourse = courseRepository.save(course);

        when(personClientAdapterImplMock.isPersonWithIdExistent(1L)).thenReturn(true);

        var courseUpdateRequest = aCourseRequest()
                .withName("Test Academy with new capacity")
                .withCapacity(15)
                .withDescription("Cool School")
                .withLecturer(1L)
                .withStart(start)
                .withEnd(end)
                .build();

        mockMvc.perform(put("/courses/{courseId}", savedCourse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(courseUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedCourse.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Academy with new capacity")))
                .andExpect(jsonPath("$.description", is("Cool School")))
                .andExpect(jsonPath("$.start", is(start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.capacity", is(15)))
                .andExpect(jsonPath("$.lecturer", is(1)));
    }

    @Test
    void updateCourseFailsForOccupiedLecturer() throws Exception {
        // TODO 11) implement test
    }

    @Test
    void updateCourseFailsForUnknownLecturer() throws Exception {
        // TODO 12) implement test
    }

    @Test
    void updateCourseFailsForUnknownCourse() throws Exception {
        var start = LocalDateTime.now();
        var end = start.plusHours(8);

        when(personClientAdapterImplMock.isPersonWithIdExistent(1L)).thenReturn(true);

        var courseUpdateRequest = aCourseRequest()
                .withName("Test Academy with new lecturer")
                .withCapacity(10)
                .withDescription("Cool School")
                .withLecturer(1L)
                .withStart(start)
                .withEnd(end)
                .build();

        mockMvc.perform(put("/courses/{courseId}", 256L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(courseUpdateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourseSuccessfully() throws Exception {
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var course = aCourse()
                .withCapacity(10)
                .withDescription("Cool School")
                .withLecturer(1L)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .build();

        var savedCourse = courseRepository.save(course);

        mockMvc.perform(get("/courses/{courseId}", savedCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedCourse.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Academy")))
                .andExpect(jsonPath("$.description", is("Cool School")))
                .andExpect(jsonPath("$.start", is(start.toString())))
                .andExpect(jsonPath("$.end", is(end.toString())))
                .andExpect(jsonPath("$.capacity", is(10)))
                .andExpect(jsonPath("$.lecturer", is(1)));
    }

    @Test
    void getCourseReturnsNotFoundOnNonExistingCourse() throws Exception {
        mockMvc.perform(get("/courses/{courseId}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourseParticipantsSuccessfully() throws Exception {
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var participantIds = Stream.of(1L, 2L)
                .collect(Collectors.toSet());
        var person1 = aPerson().withId(1L).withName("Silvio").withCompany("SwissQ").withTelephone("123").build();
        var person2 = aPerson().withId(2L).withName("Frank").withCompany("UBS").withTelephone("456").build();
        var participants = Stream.of(person1, person2)
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

        when(personClientAdapterImplMock.getPersonInformationForIds(participantIds)).thenReturn(participants);

        mockMvc.perform(get("/courses/{courseId}/participants", savedCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.participants").isArray())
                .andExpect(jsonPath("$.participants", hasSize(2)))
                .andExpect(jsonPath("$.participants[0].id", is(1)))
                .andExpect(jsonPath("$.participants[0].name", is("Silvio")))
                .andExpect(jsonPath("$.participants[0].company", is("SwissQ")))
                .andExpect(jsonPath("$.participants[0].telephone", is("123")))
                .andExpect(jsonPath("$.participants[1].id", is(2)))
                .andExpect(jsonPath("$.participants[1].name", is("Frank")))
                .andExpect(jsonPath("$.participants[1].company", is("UBS")))
                .andExpect(jsonPath("$.participants[1].telephone", is("456")));

    }

    @Test
    void getCourseParticipantsFailsForCourseNotFound() throws Exception {
        mockMvc.perform(get("/courses/{courseId}/participants", 234L))
                .andExpect(status().isNotFound());
    }
}