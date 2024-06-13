package ab.repository;

import ab.entities.AnswerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerTaskRepository extends JpaRepository<AnswerTask,Long> {
    @Query("select a from AnswerTask  a where a.task.id = :taskId and a.student.user.email=:email")
    Optional<AnswerTask> findByTaskId(Long taskId, String email);
}
