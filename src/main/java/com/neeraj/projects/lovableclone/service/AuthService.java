package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.auth.AuthResponse;
import com.neeraj.projects.lovableclone.dto.auth.LoginRequest;
import com.neeraj.projects.lovableclone.dto.auth.SignupRequest;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
