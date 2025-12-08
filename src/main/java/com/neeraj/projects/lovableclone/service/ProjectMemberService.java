package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.member.InviteMemberRequest;
import com.neeraj.projects.lovableclone.dto.member.MemberResponse;
import com.neeraj.projects.lovableclone.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    void removeProjectMember(Long projectId, Long memberId, Long userId);
}
