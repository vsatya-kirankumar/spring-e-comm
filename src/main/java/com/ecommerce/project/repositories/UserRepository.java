package com.ecommerce.project.repositories;

import com.ecommerce.project.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUserName(String username);

    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);
}