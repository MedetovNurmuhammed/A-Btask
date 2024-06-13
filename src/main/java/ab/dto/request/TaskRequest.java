package ab.dto.request;

import jakarta.validation.constraints.NotNull;

public record TaskRequest(
        @NotNull
        String title,
        @NotNull
        String description
) {
}
