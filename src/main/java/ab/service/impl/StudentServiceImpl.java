package ab.service.impl;

import ab.dto.request.StudentRequest;
import ab.dto.response.AllStudentResponse;
import ab.dto.response.SimpleResponse;
import ab.dto.response.StudentResponse;
import ab.entities.Group;
import ab.entities.Student;
import ab.entities.User;
import ab.enums.Role;
import ab.exceptions.AlreadyExistsException;
import ab.exceptions.BadRequestException;
import ab.exceptions.NotFoundException;
import ab.repository.GroupRepository;
import ab.repository.StudentRepository;
import ab.repository.UserRepository;
import ab.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SimpleResponse save(StudentRequest studentRequest, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new NotFoundException("Группа с таким id:" + groupId + "не найден"));

        boolean exists = userRepository.existsByEmail(studentRequest.email());
        if (exists)
            throw new AlreadyExistsException("Пользователь с электронной почтой " + studentRequest.email() + " уже существует");

        User user = new User();
        Student student = new Student();
        user.setFirstName(studentRequest.firstName());
        user.setLastName(studentRequest.lastName());
        user.setPhoneNumber(studentRequest.phoneNumber());
        user.setPassword(passwordEncoder.encode(studentRequest.password()));
        user.setEmail(studentRequest.email());
        student.setStudyFormat(studentRequest.studyFormat());
        user.setRole(Role.STUDENT);
        group.getStudents().add(student);
        student.setGroup(group);
        student.setUser(user);
        userRepository.save(user);
        studentRepository.save(student);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно сохранен!")
                .build();

    }

    @Override
    public AllStudentResponse findAllGroupStud(int page, int size, Long groupId) {
        if (page < 1 && size < 1) throw new BadRequestException("Page - size  страницы должен быть больше 0.");
        groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Группа c id: " + groupId + " не найден"));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<StudentResponse> studentResponses = studentRepository.findAllByGroupId(pageable, groupId);
        return AllStudentResponse.builder()
                .page(studentResponses.getNumber() + 1)
                .size(studentResponses.getNumberOfElements())
                .students(studentResponses.getContent())
                .build();
    }


    @Override
    public StudentResponse findById(Long studentId) {

        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new NotFoundException("Студент не найден! "));
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getUser().getFirstName())
                .lastName(student.getUser().getLastName())
                .phoneNumber(student.getUser().getPhoneNumber())
                .email(student.getUser().getEmail())
                .groupName(student.getGroup().getTitle())
                .studyFormat(student.getStudyFormat())
                .build();

    }

    @Override
    @Transactional
    public SimpleResponse update(Long studentId, StudentRequest studentRequest) {
        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new NotFoundException("Студент не найден! "));
        User user = student.getUser();
        if (!user.getEmail().equals(studentRequest.email())) {
            boolean b = userRepository.existsByEmail(studentRequest.email());
            if (b) throw new AlreadyExistsException("Электронная почта уже существует");
        }

        student.getUser().setFirstName(studentRequest.firstName());
        student.getUser().setLastName(studentRequest.lastName());
        student.getUser().setPhoneNumber(studentRequest.phoneNumber());
        student.getUser().setEmail(studentRequest.email());
        student.setStudyFormat(studentRequest.studyFormat());
        studentRepository.save(student);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Студент успешно обновлен!")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse delete(Long studId) {
        Student student = studentRepository.findById(studId).
                orElseThrow(() -> new NotFoundException("Студент не найден! "));
        studentRepository.delete(student);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно удален!")
                .build();
    }
}
