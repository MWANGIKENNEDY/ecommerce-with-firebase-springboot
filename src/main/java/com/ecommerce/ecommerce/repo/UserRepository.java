package com.ecommerce.ecommerce.repo;

import com.ecommerce.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // Firebase UID is the primary key (String)
}
