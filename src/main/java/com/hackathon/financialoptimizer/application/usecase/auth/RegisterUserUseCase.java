package com.hackathon.financialoptimizer.application.usecase.auth;

import com.hackathon.financialoptimizer.domain.entity.User;
import com.hackathon.financialoptimizer.domain.exception.EmailAlreadyExistsException;
import com.hackathon.financialoptimizer.domain.port.UserRepository;
import com.hackathon.financialoptimizer.infrastructure.security.JwtService;
import com.hackathon.financialoptimizer.presentation.dto.request.RegisterRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse execute(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name()
        );

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getId(), saved.getEmail());

        return new AuthResponse(token, saved.getName(), saved.getEmail());
    }
}
