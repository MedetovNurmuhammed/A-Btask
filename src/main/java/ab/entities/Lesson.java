package ab.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_gen")
    @SequenceGenerator(name = "lesson_gen", sequenceName = "lesson_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private String title;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, optional = false)
    private Group group;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "lesson", orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}

