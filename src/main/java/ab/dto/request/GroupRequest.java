package ab.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record GroupRequest(
        @NotBlank(message = "Название не должно быть пустым")
        String title,
        @Future(message = "Дата окончания должна быть в будущем")
        LocalDate dateOfEnd) {
}
