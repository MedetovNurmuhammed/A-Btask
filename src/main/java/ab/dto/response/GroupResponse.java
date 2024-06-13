package ab.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Builder
public record GroupResponse(Long id,
                            String title,
                            LocalDate dateOfStart,
                            LocalDate dateOfEnd) {
}
