package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).build();
            }

            String idToken = authHeader.substring(7);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            User user = userService.createOrUpdateUser(decodedToken);

            return ResponseEntity.ok(user);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
