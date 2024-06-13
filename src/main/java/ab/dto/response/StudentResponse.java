package ab.dto.response;

import ab.enums.StudyFormat;
import lombok.Builder;

@Builder
public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String groupName,
        StudyFormat studyFormat,
        String email
) {
}
