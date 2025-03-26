package com.round3.realestate.controller;

import com.round3.realestate.payload.DashboardResponse;
import com.round3.realestate.security.CustomUserDetails;
import com.round3.realestate.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "error", "Unauthorised: Full authentication is required to access this resource"
            ));
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(Map.of(
                "id", userDetails.getId(),
                "username", userDetails.getUsername(),
                "email", userDetails.getEmail()
        ));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(Principal principal){
        try{
            DashboardResponse dashboard = dashboardService.getUserDashboard(principal.getName());
            return ResponseEntity.ok(dashboard);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
