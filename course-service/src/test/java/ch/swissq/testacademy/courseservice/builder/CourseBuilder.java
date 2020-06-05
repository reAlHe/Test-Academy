package ch.swissq.testacademy.courseservice.builder;

import ch.swissq.testacademy.courseservice.model.Course;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public final class CourseBuilder {
    private Long id;
    private String name;
    private long lecturer;
    private Set<Long> participants = new HashSet<>();
    private int capacity;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private CourseBuilder() {
    }

    public static CourseBuilder aCourse() {
        return new CourseBuilder();
    }

    public CourseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CourseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CourseBuilder withLecturer(long lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public CourseBuilder withParticipants(Set<Long> participants) {
        this.participants = participants;
        return this;
    }

    public CourseBuilder withCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public CourseBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CourseBuilder withStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public CourseBuilder withEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public Course build() {
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setLecturer(lecturer);
        course.setParticipants(participants);
        course.setCapacity(capacity);
        course.setDescription(description);
        course.setStartTime(startTime);
        course.setEndTime(endTime);
        return course;
    }
}
