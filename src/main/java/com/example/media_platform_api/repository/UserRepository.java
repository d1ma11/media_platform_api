package com.example.media_platform_api.repository;

import com.example.media_platform_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    User findUserByUsername(String username);

    boolean existsByUsername(String userName);

    boolean existsByEmail(String email);
}
