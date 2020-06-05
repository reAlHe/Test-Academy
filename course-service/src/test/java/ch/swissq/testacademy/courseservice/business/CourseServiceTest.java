package ch.swissq.testacademy.courseservice.business;

import ch.swissq.testacademy.courseservice.model.CourseRepository;
import feign.codec.EncodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.swissq.testacademy.courseservice.builder.CourseBuilder.aCourse;
import static ch.swissq.testacademy.courseservice.builder.PersonBuilder.aPerson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CourseServiceTest {

    private CourseService underTest;

    @Mock
    private CourseRepository courseRepositoryMock;

    @Mock
    private PersonClientAdapter personClientAdapterMock;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        underTest = new CourseService(personClientAdapterMock, courseRepositoryMock);
    }

    @Test
    void registerCourseSuccessfully() {
        var lecturer = 1L;
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var course = aCourse()
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(lecturer)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .build();

        when(personClientAdapterMock.isPersonWithIdExistent(lecturer)).thenReturn(true);
        when(courseRepositoryMock.findAllOverlappingCourses(lecturer, start, end)).thenReturn(Collections.emptyList());
        when(courseRepositoryMock.save(any())).thenAnswer(returnsFirstArg());

        var result = underTest.registerCourse(course);

        assertThat(result).isEqualTo(course);
    }

    @Test
    void registerCourseFailsForUnknownLecturer() {
        var lecturer = 1L;
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var course = aCourse()
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(lecturer)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .build();

        when(personClientAdapterMock.isPersonWithIdExistent(lecturer)).thenReturn(false);

        assertThatExceptionOfType(PersonNotExistingException.class).isThrownBy(() -> underTest.registerCourse(course));
    }

    @Test
    void registerCourseFailsForLecturerHasParallelCourse() {
        var lecturer = 1L;
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var course = aCourse()
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(lecturer)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .build();
        var parallelCourse = aCourse()
                .withId(1L)
                .withLecturer(lecturer)
                .withStartTime(start)
                .withEndTime(end)
                .build();

        when(personClientAdapterMock.isPersonWithIdExistent(lecturer)).thenReturn(true);
        when(courseRepositoryMock.findAllOverlappingCourses(lecturer, start, end)).thenReturn(Collections.singletonList(parallelCourse));

        assertThatExceptionOfType(LecturerAlreadyOccupiedException.class).isThrownBy(() -> underTest.registerCourse(course));
    }


    @Test
    void updateCourseSuccessfully() {
        var oldLecturer = 1L;
        var newLecturer = 2L;
        var oldStart = LocalDateTime.now();
        var newStart = LocalDateTime.now().minusHours(1);
        var oldEnd = oldStart.plusHours(8);
        var newEnd = newStart.plusHours(12);
        var participants = Set.of(4L, 5L);
        var oldCourse = aCourse()
                .withId(1L)
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(oldLecturer)
                .withName("Test Academy")
                .withStartTime(oldStart)
                .withEndTime(oldEnd)
                .withParticipants(participants)
                .build();
        var newCourse = aCourse()
                .withCapacity(12)
                .withDescription("Cool school NEW")
                .withLecturer(newLecturer)
                .withName("Test Academy NEW")
                .withStartTime(newStart)
                .withEndTime(newEnd)
                .build();

        when(courseRepositoryMock.findById(1L)).thenReturn(Optional.of(oldCourse));
        when(personClientAdapterMock.isPersonWithIdExistent(newLecturer)).thenReturn(true);
        when(courseRepositoryMock.findAllOverlappingCourses(newLecturer, newStart, oldStart)).thenReturn(Collections.emptyList());
        when(courseRepositoryMock.save(any())).thenAnswer(returnsFirstArg());

        var result = underTest.updateCourse(1L, newCourse);

        assertThat(result).isEqualToIgnoringGivenFields(newCourse, "id", "participants");
        assertThat(result.getId()).isEqualTo(oldCourse.getId());
        assertThat(result.getParticipants()).isEqualTo(oldCourse.getParticipants());
    }

    @Test
    void updateCourseFailsForInvalidLecturer() {
        var oldLecturer = 1L;
        var newLecturer = 2L;
        var oldStart = LocalDateTime.now();
        var newStart = LocalDateTime.now().minusHours(1);
        var oldEnd = oldStart.plusHours(8);
        var newEnd = newStart.plusHours(12);
        var participants = Set.of(4L, 5L);
        var oldCourse = aCourse()
                .withId(1L)
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(oldLecturer)
                .withName("Test Academy")
                .withStartTime(oldStart)
                .withEndTime(oldEnd)
                .withParticipants(participants)
                .build();
        var newCourse = aCourse()
                .withCapacity(12)
                .withDescription("Cool school NEW")
                .withLecturer(newLecturer)
                .withName("Test Academy NEW")
                .withStartTime(newStart)
                .withEndTime(newEnd)
                .build();

        when(courseRepositoryMock.findById(1L)).thenReturn(Optional.of(oldCourse));
        when(personClientAdapterMock.isPersonWithIdExistent(newLecturer)).thenReturn(false);

        assertThatExceptionOfType(PersonNotExistingException.class).isThrownBy(() -> underTest.updateCourse(1L, newCourse));
    }

    @Test
    void updateCourseFailsForUnknownCourse() {
        var courseId = 1L;
        var newLecturer = 2L;
        var newStart = LocalDateTime.now().minusHours(1);
        var newEnd = newStart.plusHours(12);
        var newCourse = aCourse()
                .withCapacity(12)
                .withDescription("Cool school NEW")
                .withLecturer(newLecturer)
                .withName("Test Academy NEW")
                .withStartTime(newStart)
                .withEndTime(newEnd)
                .build();

        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CourseNotFoundException.class).isThrownBy(() -> underTest.updateCourse(courseId, newCourse));
    }

    @Test
    void updateCourseFailsForNewLecturerAlreadyOccupied() {
        var courseId = 1L;
        var oldLecturer = 1L;
        var newLecturer = 2L;
        var oldStart = LocalDateTime.now();
        var newStart = LocalDateTime.now().minusHours(1);
        var oldEnd = oldStart.plusHours(8);
        var newEnd = newStart.plusHours(12);
        var participants = Set.of(4L, 5L);
        var oldCourse = aCourse()
                .withId(1L)
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(oldLecturer)
                .withName("Test Academy")
                .withStartTime(oldStart)
                .withEndTime(oldEnd)
                .withParticipants(participants)
                .build();
        var newCourse = aCourse()
                .withCapacity(12)
                .withDescription("Cool school NEW")
                .withLecturer(newLecturer)
                .withName("Test Academy NEW")
                .withStartTime(newStart)
                .withEndTime(newEnd)
                .build();
        var parallelCourse = aCourse()
                .withId(8L)
                .withLecturer(newLecturer)
                .withStartTime(newStart)
                .withEndTime(newEnd)
                .build();

        when(courseRepositoryMock.findById(1L)).thenReturn(Optional.of(oldCourse));
        when(personClientAdapterMock.isPersonWithIdExistent(newLecturer)).thenReturn(true);
        when(courseRepositoryMock.findAllOverlappingCourses(newLecturer, newStart, newEnd)).thenReturn(Collections.singletonList(parallelCourse));

        assertThatExceptionOfType(LecturerAlreadyOccupiedException.class).isThrownBy(() -> underTest.updateCourse(courseId, newCourse));
    }

    @Test
    void getCourseSuccessfully() {
        var courseId = 1L;
        var lecturer = 1L;
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var course = aCourse()
                .withId(courseId)
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(lecturer)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .build();

        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(course));

        var result = underTest.getCourse(courseId);

        assertThat(result).isEqualTo(course);
    }

    @Test
    void getCourseFailsForUnknownCourse() {
        long courseId = 1L;

        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CourseNotFoundException.class).isThrownBy(() -> underTest.getCourse(courseId));
    }

    @Test
    void getCourseParticipantsSuccessfully() {
        var courseId = 1L;
        var lecturer = 1L;
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var participantIds = Stream.of(1L, 2L)
                .collect(Collectors.toSet());
        var person1 = aPerson().withId(1L).withName("Silvio").withCompany("SwissQ").withTelephone("123").build();
        var person2 = aPerson().withId(2L).withName("Frank").withCompany("UBS").withTelephone("456").build();
        var participants = Stream.of(person1, person2)
                .collect(Collectors.toSet());
        var course = aCourse()
                .withId(courseId)
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(lecturer)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .withParticipants(participantIds)
                .build();

        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(course));
        when(personClientAdapterMock.getPersonInformationForIds(participantIds)).thenReturn(participants);

        var result = underTest.getCourseParticipants(courseId);

        assertThat(result).isEqualTo(participants);
    }

    @Test
    void getCourseParticipantsThrowsExceptionForCourseNotFound() {
        when(courseRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(CourseNotFoundException.class).isThrownBy(() -> underTest.getCourseParticipants(1L));
    }

    @Test
    void getCourseParticipantsFailsForClientException() {
        var courseId = 1L;
        var lecturer = 1L;
        var start = LocalDateTime.now();
        var end = start.plusHours(8);
        var participantIds = Stream.of(1L, 2L)
                .collect(Collectors.toSet());
        var course = aCourse()
                .withId(courseId)
                .withCapacity(10)
                .withDescription("Cool school")
                .withLecturer(lecturer)
                .withName("Test Academy")
                .withStartTime(start)
                .withEndTime(end)
                .withParticipants(participantIds)
                .build();

        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(course));
        when(personClientAdapterMock.getPersonInformationForIds(participantIds)).thenThrow(new EncodeException("An error occurred"));

        assertThatExceptionOfType(EncodeException.class).isThrownBy(() -> underTest.getCourseParticipants(courseId));
    }
}