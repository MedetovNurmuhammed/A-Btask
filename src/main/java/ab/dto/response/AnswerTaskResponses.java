package ab.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnswerTaskResponses {
    private Long id;
    private String text;
}
