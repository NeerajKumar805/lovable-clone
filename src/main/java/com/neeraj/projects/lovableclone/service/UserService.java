package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
