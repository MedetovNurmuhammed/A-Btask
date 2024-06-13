package ab.repository;

import ab.dto.response.LessonResponse;
import ab.entities.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
    @Query("select new ab.dto.response.LessonResponse(i.id, i.title, i.createdAt) from Lesson i where i.group.id = :groupId ")
    Page<LessonResponse> findAllLessons(Long groupId, Pageable pageable);

    Optional<Lesson> findLessonById(Long lessonId);
}
