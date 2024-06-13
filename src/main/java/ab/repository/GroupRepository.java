package ab.repository;

import ab.dto.response.GroupResponse;
import ab.entities.Group;
import ab.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    boolean existsByTitle(String title);
    @Query("select new ab.dto.response.GroupResponse(g.id,g.title,g.dateOfStart,g.dateOfEnd)from Group g")
    Page<GroupResponse> findAllGroup(Pageable pageable);
    @Query("SELECT s FROM Student s JOIN s.group g  WHERE g.id = :id")
    List<Student> findStudentsByGroupId(Long id);

}
