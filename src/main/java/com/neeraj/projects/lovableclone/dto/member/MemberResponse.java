package com.neeraj.projects.lovableclone.dto.member;

import com.neeraj.projects.lovableclone.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        String avatarUrl,
        ProjectRole role,
        Instant invitedAt
) {
}
