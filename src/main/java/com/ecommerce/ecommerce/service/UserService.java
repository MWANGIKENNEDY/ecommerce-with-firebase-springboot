package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Role;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.repo.UserRepository;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createOrUpdateUser(FirebaseToken decodedToken) {

        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();
        String role = decodedToken.getClaims().get("role") != null
                ? decodedToken.getClaims().get("role").toString()
                : "CUSTOMER"; // fallback

        Optional<User> optionalUser = userRepository.findById(uid);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.setEmail(email);
            user.setName(name);
            user.setRole(Role.valueOf(role));
        } else {
            user = new User();
            user.setUid(uid);
            user.setEmail(email);
            user.setName(name);
            user.setRole(Role.valueOf(role)); // role from claims or CUSTOMER
        }

        return userRepository.save(user);
    }
}
