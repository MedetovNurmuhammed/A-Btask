package ab.api;

import ab.dto.request.SignInRequest;
import ab.dto.response.SignInResponse;
import ab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final UserService userService;

    @PostMapping("/Войти")
    @Operation(description = "SignIn")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest) throws AccessDeniedException {
        return userService.signIn(signInRequest);
    }
}
