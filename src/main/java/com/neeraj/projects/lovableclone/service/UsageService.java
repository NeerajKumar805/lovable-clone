package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.subscription.PlanLimitsResponse;
import com.neeraj.projects.lovableclone.dto.subscription.UsageTodayResponse;

public interface UsageService {
     UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
