package com.hck.exchange.supertraders.repository;

import com.hck.exchange.supertraders.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by @hck
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserId(String userId);
}
