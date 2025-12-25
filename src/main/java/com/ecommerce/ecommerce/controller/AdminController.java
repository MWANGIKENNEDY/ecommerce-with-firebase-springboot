package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dtos.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/setRole")
    public ResponseEntity<ApiResponse> setRole(@RequestParam String uid, @RequestParam String role) throws FirebaseAuthException {

        FirebaseAuth.getInstance().setCustomUserClaims(uid, Map.of("role", role));

        ApiResponse response = new ApiResponse("Role " + role + " assigned to user " + uid, null);
        return ResponseEntity.ok(response);
    }
}
