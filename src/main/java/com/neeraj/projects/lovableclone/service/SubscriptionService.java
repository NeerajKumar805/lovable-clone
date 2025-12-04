package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.subscription.CheckoutRequest;
import com.neeraj.projects.lovableclone.dto.subscription.CheckoutResponse;
import com.neeraj.projects.lovableclone.dto.subscription.PortalResponse;
import com.neeraj.projects.lovableclone.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
