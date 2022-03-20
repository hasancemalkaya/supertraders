package com.hck.exchange.supertraders.entity;

import com.hck.exchange.supertraders.type.TradeType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by @hck
 */

@Data
@Entity
@Table(name = "trade")
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TradeType type;

    @ManyToOne
    @JoinColumn(name = "share_id")
    private Share share;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal rate;
    private Integer amount;
    private Date date;

}
