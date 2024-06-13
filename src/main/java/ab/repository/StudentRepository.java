package ab.repository;

import ab.dto.response.StudentResponse;
import ab.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    @Query("select new ab.dto.response.StudentResponse(s.id,s.user.firstName,s.user.lastName,s.user.phoneNumber,s.group.title,s.studyFormat,s.user.email) from Student s where s.group.id = :groupId ")
    Page<StudentResponse> findAllByGroupId(Pageable pageable, Long groupId);

    @Query("select s from Student s where s.user.id =:id ")
    Optional<Student> findByUserId(Long id);
//    @Query("select s from Student s where s.id =:studentId")
//    Optional<Student> findStudentById(@Param("studentId") Long studentId);
}
