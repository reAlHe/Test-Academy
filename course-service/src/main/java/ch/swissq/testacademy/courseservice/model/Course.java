package ch.swissq.testacademy.courseservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private long lecturer;

    @Convert(converter = StringToListConverter.class)
    private Set<Long> participants = new HashSet<>();

    private int capacity;

    private String description;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    public Course(Long id, @NotBlank String name, long lecturer, @NotNull Set<Long> participants, int capacity, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.name = name;
        this.lecturer = lecturer;
        this.participants = participants;
        this.capacity = capacity;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Course withDataFrom(Course update) {
        return new Course(this.id, update.getName(), update.getLecturer(), this.getParticipants(), update.getCapacity(), update.getDescription(), update.getStartTime(), update.getEndTime());
    }
}
