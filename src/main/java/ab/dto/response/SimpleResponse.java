package ab.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Builder

public record SimpleResponse (HttpStatus httpStatus,
                              String message){
}
