package com.neeraj.projects.lovableclone.dto.auth;

public record UserProfileResponse(
        Long id,
        String email,
        String name,
        String avatarUrl
) {
}
