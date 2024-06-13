package ab.service;

import ab.dto.request.LessonRequest;
import ab.dto.response.AllLessonsResponse;
import ab.dto.response.LessonResponse;
import ab.dto.response.SimpleResponse;

public interface LessonService {
    SimpleResponse addLesson(LessonRequest lessonRequest, Long groupId);

    AllLessonsResponse findAll(int page, int size, Long groupId);

    SimpleResponse update(LessonRequest lessonRequest, Long lessonId);

    SimpleResponse delete(Long lessonId);

    LessonResponse findById(Long lessonId);
}
