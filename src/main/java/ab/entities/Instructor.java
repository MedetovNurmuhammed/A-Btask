package ab.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instructor_gen")
    @SequenceGenerator(name = "instructor_gen", sequenceName = "instructor_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private String specialization;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private User user;
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
