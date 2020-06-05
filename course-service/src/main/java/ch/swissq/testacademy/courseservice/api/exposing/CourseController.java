package ch.swissq.testacademy.courseservice.api.exposing;

import ch.swissq.testacademy.courseservice.api.exposing.dto.CourseRequest;
import ch.swissq.testacademy.courseservice.api.exposing.dto.CourseResponse;
import ch.swissq.testacademy.courseservice.api.exposing.dto.ParticipantsResponse;
import ch.swissq.testacademy.courseservice.business.CourseNotFoundException;
import ch.swissq.testacademy.courseservice.business.CourseService;
import ch.swissq.testacademy.courseservice.business.LecturerAlreadyOccupiedException;
import ch.swissq.testacademy.courseservice.business.PersonNotExistingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ch.swissq.testacademy.courseservice.api.exposing.dto.CourseResponse.fromCourse;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/register")
    public ResponseEntity<CourseResponse> registerCourse(@RequestBody CourseRequest courseRequest) {
        var registeredCourse = courseService.registerCourse(courseRequest.asCourse());
        return new ResponseEntity<>(fromCourse(registeredCourse), HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable long courseId, @RequestBody CourseRequest courseRequest) {
        var updatedCourse = courseService.updateCourse(courseId, courseRequest.asCourse());
        return new ResponseEntity<>(fromCourse(updatedCourse), HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable long courseId) {
        var foundCourse = courseService.getCourse(courseId);
        return new ResponseEntity<>(fromCourse(foundCourse), HttpStatus.OK);
    }

    @GetMapping("/{courseId}/participants")
    public ResponseEntity<ParticipantsResponse> getCourseParticipants(@PathVariable long courseId) {
        var participants = courseService.getCourseParticipants(courseId);
        var participantsResponse = ParticipantsResponse.builder().participants(participants).build();
        return new ResponseEntity<>(participantsResponse, HttpStatus.OK);
    }

    @ExceptionHandler({LecturerAlreadyOccupiedException.class, PersonNotExistingException.class})
    public ResponseEntity<Void> handleInvalidCourseInformation() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({CourseNotFoundException.class})
    public ResponseEntity<Void> handleCourseNotFound() {
        return ResponseEntity.notFound().build();
    }

}
