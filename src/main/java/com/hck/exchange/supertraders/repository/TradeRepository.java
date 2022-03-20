package com.hck.exchange.supertraders.repository;

import com.hck.exchange.supertraders.entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by @hck
 */
public interface TradeRepository extends JpaRepository<Trade, UUID> {

    Page<Trade> findAllByShareId(Pageable pageable, UUID shareId);
}
