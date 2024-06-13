package ab.api;

import ab.dto.request.StudentRequest;
import ab.dto.response.AllStudentResponse;
import ab.dto.response.SimpleResponse;
import ab.dto.response.StudentResponse;
import ab.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentApi {
    private final StudentService studentService;
    @Secured("INSTRUCTOR")
    @PostMapping("addStudent/{groupId}")
    @Operation(summary = "Добавить студента в группу!",
            description =
                    " Авторизация: инструктор!")
    public SimpleResponse saveStudent(@RequestBody @Valid StudentRequest studentRequest, @PathVariable Long groupId)  {

        return studentService.save(studentRequest, groupId);
    }
    @Operation(summary = "Получить все студенты!", description = "Метод для получение всex студентов по " +
            "их group_id ! Авторизация: студент и инструктор!")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    @GetMapping("group/{groupId}")
    public AllStudentResponse findAllGroupStud(@RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "12") int size,
                                               @PathVariable Long groupId) {
        return studentService.findAllGroupStud(page, size, groupId);
    }
    @Operation(summary = "Получить информацию о студенте по идентификатору ",
            description = "Метод для получения информации о студенте по его идентификатору." +
                    " Авторизация:  инструктор!")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR')")
    @GetMapping("/{studentId}")
    public StudentResponse findById(@PathVariable Long studentId) {
        return studentService.findById(studentId);
    }
    @Operation(summary = "Обновить информацию о студенте",
            description = "Метод для обновления информации о студенте по его идентификатору." +
                    " Авторизация:  инструктор!")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR')")
    @PatchMapping("/{studentId}")
    public SimpleResponse update(@PathVariable Long studentId,
                                 @RequestBody StudentRequest studentRequest) {
        return studentService.update(studentId, studentRequest);
    }
    @Operation(summary = "Удалить cтудента",
            description = "Метод для удаления cтудента по его идентификатору." +
                    " Авторизация: инструктор!")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR')")
    @DeleteMapping("/{studentId}")
    public SimpleResponse delete(@PathVariable Long studentId) {
        return studentService.delete(studentId);
    }
}

