package ab.dto.response;

import lombok.Builder;

import java.util.List;
@Builder
public record AllLessonsResponse(
        int page,
        int size,
        List<LessonResponse> lessonResponses
) {
}
