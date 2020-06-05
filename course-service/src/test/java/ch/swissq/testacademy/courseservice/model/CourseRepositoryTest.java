package ch.swissq.testacademy.courseservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static ch.swissq.testacademy.courseservice.builder.CourseBuilder.aCourse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CourseRepositoryTest {

    @Autowired
    private CourseRepository underTest;

    private final long LECTURER = 1L;

    private final LocalDateTime START = LocalDateTime.now();

    private final LocalDateTime END = START.plusHours(8);

    @BeforeEach
    void init() {
        underTest.deleteAll();

        Course course = aCourse()
                .withCapacity(10)
                .withLecturer(LECTURER)
                .withName("Test Academy")
                .withStartTime(START)
                .withEndTime(END)
                .build();

        underTest.save(course);
    }

    @ParameterizedTest
    @MethodSource("overlapping")
    void findParallelOverlappingCourse(int startOffset, int endOffset) {
        List<Course> result = underTest.findAllOverlappingCourses(LECTURER, START.plusHours(startOffset), END.plusHours(endOffset));
        assertThat(result).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("seamless")
    void doesNotConsiderSeamlessFollowUps(int startOffset, int endOffset) {
        List<Course> result = underTest.findAllOverlappingCourses(LECTURER, START.plusHours(startOffset), END.plusHours(endOffset));
        assertThat(result).isEmpty();
    }

    private static Stream<Arguments> overlapping() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(0, 1),
                Arguments.of(1, 0),
                Arguments.of(1, 1),
                Arguments.of(-1, 0),
                Arguments.of(0, -1),
                Arguments.of(-1, -1)
                );
    }

    private static Stream<Arguments> seamless() {
        return Stream.of(
                Arguments.of(-8, -8),
                Arguments.of(8, 8)
        );
    }
}