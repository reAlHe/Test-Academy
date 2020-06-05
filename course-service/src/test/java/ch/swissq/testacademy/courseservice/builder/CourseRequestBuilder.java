package ch.swissq.testacademy.courseservice.builder;

import ch.swissq.testacademy.courseservice.api.exposing.dto.CourseRequest;

import java.time.LocalDateTime;

public final class CourseRequestBuilder {
    private String name;
    private long lecturer;
    private int capacity;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;

    private CourseRequestBuilder() {
    }

    public static CourseRequestBuilder aCourseRequest() {
        return new CourseRequestBuilder();
    }

    public CourseRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CourseRequestBuilder withLecturer(long lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public CourseRequestBuilder withCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public CourseRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CourseRequestBuilder withStart(LocalDateTime start) {
        this.start = start;
        return this;
    }

    public CourseRequestBuilder withEnd(LocalDateTime end) {
        this.end = end;
        return this;
    }

    public CourseRequest build() {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(name);
        courseRequest.setLecturer(lecturer);
        courseRequest.setCapacity(capacity);
        courseRequest.setDescription(description);
        courseRequest.setStart(start);
        courseRequest.setEnd(end);
        return courseRequest;
    }
}
