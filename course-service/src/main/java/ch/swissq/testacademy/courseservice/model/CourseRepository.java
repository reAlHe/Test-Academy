package ch.swissq.testacademy.courseservice.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Query("SELECT u FROM Course u WHERE u.lecturer = ?1 AND ((u.startTime > ?2 AND u.startTime < ?3) OR (u.endTime > ?2 AND u.endTime < ?3) OR (u.startTime <= ?2 AND u.endTime >= ?3))")
    List<Course> findAllOverlappingCourses(long lecturer, LocalDateTime start, LocalDateTime end);
}
