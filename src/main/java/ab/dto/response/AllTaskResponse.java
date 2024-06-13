package ab.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AllTaskResponse {
    List<TaskResponse> taskResponse;

}
