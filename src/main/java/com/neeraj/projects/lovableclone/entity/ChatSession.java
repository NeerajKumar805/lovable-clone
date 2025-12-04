package com.neeraj.projects.lovableclone.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatSession {

    com.neeraj.projects.lovableclone.entity.Project project;

    User user;

    String title;

    Instant createdAt;
    Instant updatedAt;

    Instant deletedAt; //soft delete
}
