package ab.dto.request;

import ab.enums.StudyFormat;
import ab.validation.phoneNumber.PhoneNumberValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest (
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @NotBlank
    String password,
    @PhoneNumberValidation(message = "номер должен содержать +996 и 13 символов ")
    String phoneNumber,
    @Email
    String email,

@NotNull(message = "Формат обучения не должно быть пустым!")
    StudyFormat studyFormat){
}
