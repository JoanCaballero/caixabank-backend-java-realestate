package com.round3.realestate.repository;

import com.round3.realestate.payload.AuthResponse;
import com.round3.realestate.payload.LoginRequest;
import com.round3.realestate.payload.RegisterRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository {
    AuthResponse register (RegisterRequest request);
    AuthResponse login (LoginRequest request) throws BadCredentialsException;
}
