package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.entity.ProjectMember;
import com.neeraj.projects.lovableclone.dto.member.InviteMemberRequest;
import com.neeraj.projects.lovableclone.dto.member.MemberResponse;

import java.util.List;

public interface ProjectMemberService {
    List<ProjectMember> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, InviteMemberRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);
}
