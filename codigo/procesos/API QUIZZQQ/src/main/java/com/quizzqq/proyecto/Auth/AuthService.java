package com.quizzqq.proyecto.Auth;

import com.quizzqq.proyecto.User.UserRepositoryImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quizzqq.proyecto.Jwt.JwtService;
import com.quizzqq.proyecto.User.User;
import com.quizzqq.proyecto.User.Role;
import java.sql.SQLException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryImpl userRepository; // Inyectar el repositorio
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getClave()));

        User user = userRepository.findByUsername(request.getUsuario())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .usuario(request.getUsuario())
                .clave(passwordEncoder.encode(request.getClave()))
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .estado("A")
                .role(Role.USER)
                .build();

        try {
            userRepository.save(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar usuario", e);
        }

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}