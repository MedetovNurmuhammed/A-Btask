package ab.api;

import ab.dto.request.LessonRequest;
import ab.dto.response.AllLessonsResponse;
import ab.dto.response.LessonResponse;
import ab.dto.response.SimpleResponse;
import ab.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lessons")
public class LessonApi {
    private final LessonService lessonService;

    @Secured("INSTRUCTOR")
    @Operation(summary = "добавляет урок.(Авторизация: инструктор)")
    @PostMapping("/{groupId}")
    public SimpleResponse addLesson(@RequestBody @Valid LessonRequest lessonRequest, @PathVariable Long groupId) {
        return lessonService.addLesson(lessonRequest, groupId);
    }

    @Secured({"INSTRUCTOR", "STUDENT"})
    @Operation(summary = "Возвращает пагинированный список всех уроков.(Авторизация: инструктор и студент)")
    @GetMapping("all/{courseId}")
    public AllLessonsResponse findAll(@RequestParam(required = false, defaultValue = "1") int page,
                                      @RequestParam(required = false, defaultValue = "12") int size, @PathVariable Long courseId) {
        return lessonService.findAll(page, size, courseId);
    }

    @Secured({"INSTRUCTOR", "STUDENT"})
    @Operation(summary = "Возвращает урок.(Авторизация: инструктор и студент)")
    @GetMapping("/{lessonId}")
    public LessonResponse findById(@PathVariable Long lessonId) {
        return lessonService.findById(lessonId);
    }

    @Secured("INSTRUCTOR")
    @PatchMapping("/{lessonId}")
    @Operation(summary = "Обновляет информацию о уроке.(Авторизация: инструктор)")
    public SimpleResponse update(
            @RequestBody @Valid LessonRequest lessonRequest, @PathVariable Long lessonId) {
        return lessonService.update(lessonRequest, lessonId);
    }

    @Secured("INSTRUCTOR")
    @DeleteMapping("/{lessonId}")
    @Operation(summary = "Удаляет текущий урок.(Авторизация: инструктор)")
    public SimpleResponse delete(@PathVariable Long lessonId) {
        return lessonService.delete(lessonId);
    }
}


