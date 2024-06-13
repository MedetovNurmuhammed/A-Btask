package ab.api;

import ab.dto.request.TaskRequest;
import ab.dto.response.AllTaskResponse;
import ab.dto.response.SimpleResponse;
import ab.dto.response.TaskResponse;
import ab.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskApi {
    private final TaskService taskService;

    @Secured("INSTRUCTOR")
    @Operation(summary = "Создание задания для урока", description = "Создает новое задание для указанного урока.")
    @PostMapping("/{lessonId}")
    public SimpleResponse createTask(@PathVariable("lessonId") Long lessonId,
                                     @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.createTask(lessonId, taskRequest);
    }

    @Secured({"INSTRUCTOR", "STUDENT"})
    @Operation(summary = "Поиск задания по идентификатору", description = "Находит задание по указанному идентификатору.")
    @GetMapping("/{taskId}")
    public TaskResponse findById(@PathVariable("taskId") Long taskId) {
        return taskService.findById(taskId);
    }

    @Secured("INSTRUCTOR")
    @Operation(summary = "Обновление задания по идентификатору", description = "Обновляет существующее задание по указанному идентификатору.")
    @PatchMapping("/{taskId}")
    public SimpleResponse update(@PathVariable Long taskId, @RequestBody @Valid TaskRequest taskRequest) {
        return taskService.updateTask(taskId, taskRequest);
    }

    @Secured("INSTRUCTOR")
    @Operation(summary = "Удаление задания", description = "Удаляет задание по указанному идентификатору.")
    @DeleteMapping("/{taskId}")
    public SimpleResponse delete(@PathVariable("taskId") Long taskId) {
        return taskService.deleteTask(taskId);
    }

    @Secured({"INSTRUCTOR", "STUDENT"})
    @Operation(summary = "Поиск заданий по идентификатору урока", description = "Находит все задания для указанного урока.")
    @GetMapping("/taskOfLesson/{lessonId}")
    public ResponseEntity<AllTaskResponse> findTaskByLessonId(@PathVariable("lessonId") Long lessonId) {
        try {
            AllTaskResponse response = taskService.findAllTaskByLessonId(lessonId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
