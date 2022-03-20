package com.hck.exchange.supertraders.repository;

import com.hck.exchange.supertraders.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by @hck
 */

@Repository
public interface ShareRepository extends JpaRepository<Share, UUID> {

    Optional<Share> findBySymbol(String symbol);
}
