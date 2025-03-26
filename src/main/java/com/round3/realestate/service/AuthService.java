package com.round3.realestate.service;

import com.round3.realestate.entity.enumerations.Role;
import com.round3.realestate.entity.User;
import com.round3.realestate.payload.LoginRequest;
import com.round3.realestate.payload.RegisterRequest;
import com.round3.realestate.payload.AuthResponse;
import com.round3.realestate.repository.AuthRepository;
import com.round3.realestate.repository.UserRepository;
import com.round3.realestate.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthService implements AuthRepository {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final JwtProvider jwtProvider;

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
    public AuthResponse login(LoginRequest request) throws BadCredentialsException {

        User user = userRepository.findUserByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new BadCredentialsException("Unauthorised: Bad credentials"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Unauthorised: Bad credentials");
        }

        String token = jwtProvider.generateToken(user.getUsername());

        return new AuthResponse(true, token);
    }
}
