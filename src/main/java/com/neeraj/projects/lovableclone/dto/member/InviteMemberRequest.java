package com.neeraj.projects.lovableclone.dto.member;

import com.neeraj.projects.lovableclone.enums.ProjectRole;

public record InviteMemberRequest(
        String email,
        ProjectRole role
) {
}
