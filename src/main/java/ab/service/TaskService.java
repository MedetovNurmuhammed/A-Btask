package ab.service;

import ab.dto.request.TaskRequest;
import ab.dto.response.AllTaskResponse;
import ab.dto.response.SimpleResponse;
import ab.dto.response.TaskResponse;

public interface TaskService {
    SimpleResponse createTask(Long lessonId, TaskRequest taskRequest);

    TaskResponse findById(Long taskId);

    SimpleResponse updateTask(Long taskId, TaskRequest taskRequest);

    SimpleResponse deleteTask(Long taskId);

    AllTaskResponse findAllTaskByLessonId(Long lessonId);
}
