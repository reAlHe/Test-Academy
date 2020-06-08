package ch.swissq.testacademy.courseservice.business;

import ch.swissq.testacademy.courseservice.model.Course;
import ch.swissq.testacademy.courseservice.model.CourseRepository;
import ch.swissq.testacademy.courseservice.model.Person;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class CourseService {

    private final PersonClientAdapter personClientAdapter;

    private final CourseRepository courseRepository;

    public Course registerCourse(Course courseToRegister) {
        validateCourseInformation(courseToRegister);
        return courseRepository.save(courseToRegister);
    }

    public Course updateCourse(long courseId, Course courseUpdate) {
        // TODO 3) implement update logic
        return null;
    }

    public Course getCourse(long courseId) {
        var foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isEmpty()) {
            throw new CourseNotFoundException();
        }
        return foundCourse.get();
    }

    public Set<Person> getCourseParticipants(long courseId) {
        // TODO 6) implement logic for retrieving detailed information about the participants
        return null;
    }

    private void validateCourseInformation(Course course) {
        if (!isPersonExisting(course.getLecturer())) {
            throw new PersonNotExistingException();
        }
        if (!isLecturerAvailable(course)) {
            throw new LecturerAlreadyOccupiedException();
        }
    }

    private boolean isLecturerAvailable(Course course) {
        var foundCourses = courseRepository.findAllOverlappingCourses(course.getLecturer(), course.getStartTime(), course.getEndTime());
        return foundCourses.isEmpty() || (foundCourses.size() == 1 && Objects.equals(foundCourses.get(0).getId(), course.getId()));
    }

    private boolean isPersonExisting(long personId) {
        return personClientAdapter.isPersonWithIdExistent(personId);
    }
}
