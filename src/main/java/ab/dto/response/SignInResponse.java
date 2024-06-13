package ab.dto.response;

import ab.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record SignInResponse(Long id,
                             String token,
                             String email,
                             @Enumerated(EnumType.STRING)
                           Role role,
                             HttpStatus httpStatus,
                             String message

) {
}
