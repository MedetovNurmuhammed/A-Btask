package ab.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
}
