package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.auth.UserProfileResponse;
import org.jspecify.annotations.Nullable;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
