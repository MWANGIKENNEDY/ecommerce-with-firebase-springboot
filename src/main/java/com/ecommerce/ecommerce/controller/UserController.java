package com.ecommerce.ecommerce.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/profile")
    public Map<String, Object> getProfile(Authentication auth) {
        String uid = auth.getName(); // Firebase UID

        return Map.of(
                "uid", uid,
                "message", "Authenticated successfully!"
        );
    }

    @GetMapping("/public/hello")
    public String publicEndpoint() {
        return "This endpoint is public.";
    }
}
