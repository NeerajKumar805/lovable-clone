package com.neeraj.projects.lovableclone.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
