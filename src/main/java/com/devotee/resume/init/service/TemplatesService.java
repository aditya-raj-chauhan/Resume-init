package com.devotee.resume.init.service;

import com.devotee.resume.init.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplatesService {

    private final AuthService authService;

    public Map<String, Object> getTemplates(
            Object principal
    ) {

        // Step 1: get current profile
        AuthResponse response =
                authService.getProfile(principal);

        // Step 2: identify subscription
        List<String> allTemplates =
                Arrays.asList(
                        "01",
                        "02",
                        "03"
                );

        List<String> availableTemplates;

        Boolean isPremium =
                "premium".equalsIgnoreCase(
                        response.getSubscriptionPlan()
                );

        if (isPremium) {

            availableTemplates =
                    allTemplates;

        } else {

            availableTemplates =
                    List.of("01");
        }

        // Step 3: add data into map
        Map<String, Object> restrictions =
                new HashMap<>();

        restrictions.put(
                "availableTemplates",
                availableTemplates
        );

        restrictions.put(
                "allTemplates",
                allTemplates
        );

        restrictions.put(
                "subscriptionPlan",
                response.getSubscriptionPlan()
        );

        restrictions.put(
                "isPremium",
                isPremium
        );

        // Step 4: return result
        return restrictions;
    }
}