package ab.service.impl;

import ab.dto.request.LessonRequest;
import ab.dto.response.AllLessonsResponse;
import ab.dto.response.LessonResponse;
import ab.dto.response.SimpleResponse;
import ab.entities.Group;
import ab.entities.Lesson;
import ab.exceptions.NotFoundException;
import ab.repository.GroupRepository;
import ab.repository.LessonRepository;
import ab.service.LessonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final GroupRepository groupRepository;
    public final LessonRepository lessonRepository;

    @Override
    @Transactional
    public SimpleResponse addLesson(LessonRequest lessonRequest, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Курс c " + groupId + " не найден"));
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonRequest.getTitle());
        lesson.setGroup(group);
        lessonRepository.save(lesson);
        groupRepository.save(group);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Урок " + lesson.getTitle() + " успешно сохранено")
                .build();
    }

    @Override
    public AllLessonsResponse findAll(int page, int size, Long groupId) {
        if (page < 1 && size < 1)
            throw new java.lang.IllegalArgumentException("Индекс страницы не должен быть меньше нуля");
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<LessonResponse> allLessons = lessonRepository.findAllLessons(groupId, pageable);
        return AllLessonsResponse.builder()
                .page(allLessons.getNumber() + 1)
                .size(allLessons.getNumberOfElements())
                .lessonResponses(allLessons.getContent())
                .build();
    }

    @Override
    public SimpleResponse update(LessonRequest lessonRequest, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Урок c " + lessonId + " не найден"));
        lesson.setTitle(lessonRequest.getTitle());
        lessonRepository.save(lesson);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("урок " + lesson.getTitle() + " успешно обновлён")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse delete(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок c " + lessonId + " не найден"));
        lessonRepository.delete(lesson);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Урок успешно удален!!")
                .build();
    }

    @Override
    public LessonResponse findById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Урок c " + lessonId + " не найден"));
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .createdAt(lesson.getCreatedAt())
                .build();
    }
}
