package com.hck.exchange.supertraders.repository;

import com.hck.exchange.supertraders.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by @hck
 */

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
}
