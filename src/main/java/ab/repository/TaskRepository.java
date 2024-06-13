package ab.repository;

import ab.dto.response.TaskResponse;
import ab.entities.Lesson;
import ab.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query("select t from Task t where t.lesson.id=:lessonId ")
    List<Task> findAll(Long lessonId);
    @Query("select t from Task t where t.lesson.id = :lessonId")
    List<Task > findAllByLessonId(@Param("lessonId") Long lessonId);
}
