package ab.api;

import ab.dto.request.AnswerTaskRequest;
import ab.dto.response.AnswerTaskResponses;
import ab.dto.response.SimpleResponse;
import ab.service.AnswerTaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AnswerTaskApi {
    private final AnswerTaskService answerTaskService;

    @Secured("STUDENT")
    @Operation(summary = "Сохранение ответа на задание",
            description = "Сохраняет ответ на задание для указанного идентификатора задания. " +
                    " Авторизация: студент!")
    @PostMapping("/send-answer/{answerTaskId}")
    public SimpleResponse save(@PathVariable("answerTaskId") Long answerTaskId, @RequestBody AnswerTaskRequest answerTaskRequest) throws AccessDeniedException {
        return answerTaskService.save(answerTaskId, answerTaskRequest);
    }

    @Secured("STUDENT")
    @Operation(summary = "Поиск ответа на задание по идентификатору задания",
            description = "Возвращает ответ на задание для указанного идентификатора задания." +
                    " Авторизация: студент!")
    @GetMapping("/{taskId}")
    public AnswerTaskResponses findAnswerByTaskId(@PathVariable("taskId") Long taskId) {
        return answerTaskService.findAnswerByTaskId(taskId);
    }
}
