package ch.swissq.testacademy.courseservice.api.exposing.dto;

import ch.swissq.testacademy.courseservice.model.Course;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CourseResponse {

    private long id;

    private String name;

    private long lecturer;

    private int capacity;

    private String description;

    private Set<Long> participants;

    private LocalDateTime start;

    private LocalDateTime end;

    public static CourseResponse fromCourse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .lecturer(course.getLecturer())
                .capacity(course.getCapacity())
                .participants(course.getParticipants())
                .description(course.getDescription())
                .start(course.getStartTime())
                .end(course.getEndTime())
                .build();
    }
}
