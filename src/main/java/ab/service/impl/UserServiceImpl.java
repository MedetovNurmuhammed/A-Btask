package ab.service.impl;

import ab.config.jwt.JwtService;
import ab.dto.request.SignInRequest;
import ab.dto.response.SignInResponse;
import ab.entities.User;
import ab.exceptions.NotFoundException;
import ab.repository.UserRepository;
import ab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws AccessDeniedException {
        User user = userRepository.findByEmail(signInRequest.getLogin()).orElseThrow(()
                -> new AccessDeniedException("Пользователь с электронной почтой " + signInRequest.getLogin() + " не найден"));
        boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
        if (!matches) throw new NotFoundException("Неверный пароль");
        return SignInResponse.builder()
                .token(jwtService.createToken(user))
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .httpStatus(HttpStatus.OK)
                .message("Успешный вход")
                .build();
    }
}
