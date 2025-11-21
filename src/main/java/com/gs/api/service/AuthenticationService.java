package com.gs.api.service;

import com.gs.api.dto.request.LoginRequest;
import com.gs.api.dto.response.LoginResponse;
import com.gs.api.dto.request.RegisterRequest;
import com.gs.api.enums.Role;
import com.gs.api.model.User;
import com.gs.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username já existe");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já existe");
        }

        User user = User.builder()
                .name(request.getName())          
                .telephone(request.getTelephone())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new LoginResponse("Usuário registrado com sucesso", user.getUsername(), token);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return new LoginResponse("Login realizado com sucesso", user.getUsername(), token);
    }
}
