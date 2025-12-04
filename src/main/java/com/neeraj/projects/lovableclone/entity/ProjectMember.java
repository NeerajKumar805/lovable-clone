package com.neeraj.projects.lovableclone.entity;

import com.neeraj.projects.lovableclone.enums.ProjectRole;

import java.time.Instant;

public class ProjectMember {

    ProjectMemberId id;

    Project project;

    User user;

    ProjectRole projectRole;

    Instant invitedAt;
    Instant acceptedAt;
}
