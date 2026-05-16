package com.hackathon.financialoptimizer.application.usecase.auth;

import com.hackathon.financialoptimizer.domain.entity.User;
import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.UserRepository;
import com.hackathon.financialoptimizer.infrastructure.security.JwtService;
import com.hackathon.financialoptimizer.presentation.dto.request.LoginRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse execute(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuário", request.email()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail());
    }
}
