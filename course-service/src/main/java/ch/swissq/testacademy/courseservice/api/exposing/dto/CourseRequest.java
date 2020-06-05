package ch.swissq.testacademy.courseservice.api.exposing.dto;

import ch.swissq.testacademy.courseservice.model.Course;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;

@Data
public class CourseRequest {

    private String name;

    private long lecturer;

    private int capacity;

    private String description;

    private LocalDateTime start;

    private LocalDateTime end;

    public Course asCourse() {
        return new Course(null, name, lecturer, Collections.emptySet(), capacity, description, start, end);
    }
}
