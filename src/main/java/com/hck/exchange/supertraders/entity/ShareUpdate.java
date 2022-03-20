package com.hck.exchange.supertraders.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by @hck
 */

@Data
@Entity
@Table(name = "share_update")
@NoArgsConstructor
public class ShareUpdate {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "share_id")
    private Share share;

    private BigDecimal newRate;

    @Override
    public String toString() {
        return "ShareUpdate{" +
                "id=" + id +
                ", newRate=" + newRate +
                '}';
    }
}
