package ab.entities;

import ab.enums.StudyFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_gen")
    @SequenceGenerator(name = "student_gen", sequenceName = "student_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @OneToOne(cascade = CascadeType.REMOVE,orphanRemoval = true, fetch =  FetchType.LAZY)
    private User user;
    @ManyToOne(cascade = CascadeType.DETACH,optional = false,fetch = FetchType.LAZY)
    private Group group;
    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE,orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<AnswerTask> answerTasks = new ArrayList<>();
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
}
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}