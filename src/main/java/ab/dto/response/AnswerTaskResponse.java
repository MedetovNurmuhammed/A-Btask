package ab.dto.response;

import lombok.Builder;

@Builder
public record AnswerTaskResponse (
        String text
){
}
