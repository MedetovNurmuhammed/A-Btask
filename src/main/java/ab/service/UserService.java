package ab.service;

import ab.dto.request.SignInRequest;
import ab.dto.response.SignInResponse;

import java.nio.file.AccessDeniedException;

public interface UserService {
    SignInResponse signIn(SignInRequest signInRequest) throws AccessDeniedException;
}
