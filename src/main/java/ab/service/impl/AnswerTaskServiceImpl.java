package ab.service.impl;

import ab.dto.request.AnswerTaskRequest;
import ab.dto.response.AnswerTaskResponses;
import ab.dto.response.SimpleResponse;
import ab.entities.AnswerTask;
import ab.entities.Student;
import ab.entities.User;
import ab.exceptions.NotFoundException;
import ab.repository.AnswerTaskRepository;
import ab.repository.StudentRepository;
import ab.repository.TaskRepository;
import ab.repository.UserRepository;
import ab.service.AnswerTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AnswerTaskServiceImpl implements AnswerTaskService {
    private final UserRepository userRepository;
    private final AnswerTaskRepository answerTaskRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;

    @Override
    public SimpleResponse save(Long answerTaskId, AnswerTaskRequest answerTaskRequest) throws AccessDeniedException {
        User currentUser = getCurrentUser();
        Student student = studentRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new NoSuchElementException("Студент не найден"));

        AnswerTask answer = findAnswerTaskById(answerTaskId);

        if (!answer.getStudent().getId().equals(student.getId())) {
            throw new ab.exceptions.AccessDeniedException("Студент не имеет прав на изменение этого ответа на задание");
        }

        answer.setCompleted(true);
        answer.setText(answerTaskRequest.text());
        answerTaskRepository.save(answer);

        return SimpleResponse.builder()
                .message("Ответ на задание успешно обновлен")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public AnswerTaskResponses findAnswerByTaskId(Long taskId) {
        taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Задание не найдено"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AnswerTask answerTask = answerTaskRepository.findByTaskId(taskId, email).orElseThrow(() -> new NoSuchElementException("Ответ на задание не найден для данного пользователя"));
        return AnswerTaskResponses.builder()
                .id(answerTask.getId())
                .text(answerTask.getText())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private AnswerTask findAnswerTaskById(Long answerTaskId) {
        return answerTaskRepository.findById(answerTaskId)
                .orElseThrow(() -> new NotFoundException("Ответ на задание не найден"));
    }
}
