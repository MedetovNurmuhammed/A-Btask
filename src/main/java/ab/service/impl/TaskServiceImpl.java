package ab.service.impl;

import ab.dto.request.TaskRequest;
import ab.dto.response.AllTaskResponse;
import ab.dto.response.SimpleResponse;
import ab.dto.response.TaskResponse;
import ab.entities.AnswerTask;
import ab.entities.Lesson;
import ab.entities.Student;
import ab.entities.Task;
import ab.exceptions.NotFoundException;
import ab.repository.*;
import ab.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final GroupRepository groupRepository;
    private final LessonRepository lessonRepository;
    private final TaskRepository taskRepository;
    private final AnswerTaskRepository answerTaskRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public SimpleResponse createTask(Long lessonId, TaskRequest taskRequest) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("Урок не существует"));
        Task task = new Task();
        task.setLesson(lesson);
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        List<Student>students = groupRepository.findStudentsByGroupId(lesson.getGroup().getId());
        for (Student student : students) {
            AnswerTask answerTask = new AnswerTask();
            answerTask.setCompleted(false);
            answerTask.setText(null);
            answerTask.setTask(task);
            answerTask.setStudent(student);
            answerTaskRepository.save(answerTask);
            studentRepository.save(student);
        }
        taskRepository.save(task);
        lesson.getTasks().add(task);
        lessonRepository.save(lesson);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно создана")
                .build();
    }


    @Override
    public TaskResponse findById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Задание с id: " + taskId + " не найден!"));
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }

    @Override
    public SimpleResponse updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Задание с id: " + taskId + " не найден!"));
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        taskRepository.save(task);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно обновлено")
                .build();
    }

    @Override
    public SimpleResponse deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Задание с id: " + taskId + " не найден!"));
        taskRepository.delete(task);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно удалено")
                .build();
    }

    @Override
    public AllTaskResponse findAllTaskByLessonId(Long lessonId) {
        lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Урок не существует"));
        List<Task> tasks = taskRepository.findAllByLessonId(lessonId);
        List<TaskResponse> taskResponses = new ArrayList<>();

        for (Task task : tasks) {
            taskResponses.add(convertToTaskResponse(task));
        }

        return AllTaskResponse.builder()
                .taskResponse(taskResponses)
                .build();
    }



    private TaskResponse convertToTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }
}

