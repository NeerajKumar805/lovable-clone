package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.subscription.PlanResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface PlanService {
     List<PlanResponse> getAllActivePlans();
}
