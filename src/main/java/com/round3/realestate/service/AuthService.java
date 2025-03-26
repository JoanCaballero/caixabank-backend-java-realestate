package com.round3.realestate.service;

import com.round3.realestate.entity.enumerations.Role;
import com.round3.realestate.entity.User;
import com.round3.realestate.payload.LoginRequest;
import com.round3.realestate.payload.RegisterRequest;
import com.round3.realestate.payload.AuthResponse;
import com.round3.realestate.repository.AuthRepository;
import com.round3.realestate.repository.UserRepository;
import com.round3.realestate.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements AuthRepository {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse(false, "The username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmployed(false);
        user.setRole(Role.USER);

        userRepository.save(user);
        String jwtToken = jwtProvider.generateToken(user.getUsername());
        return new AuthResponse(true, jwtToken, "User successfully registered");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findUserByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtProvider.generateToken(user.getUsername());
                return new AuthResponse(true, token);
            }
        }
        return new AuthResponse(false, "Unauthorised: Bad credentials");
    }
}
