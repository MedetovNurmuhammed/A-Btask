package ab.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answer_tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_task_gen")
    @SequenceGenerator(name = "answer_task_gen", sequenceName = "answer_task_seq", allocationSize = 1, initialValue = 21)
    private long id;
    private String text;
    private Boolean completed;
    private LocalDateTime dateOfSend;
    private LocalDateTime updatedAt;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Task task;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Student student;

    @PrePersist
    protected void onCreate() {
        dateOfSend = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateOfSend = LocalDateTime.now();
    }

}
