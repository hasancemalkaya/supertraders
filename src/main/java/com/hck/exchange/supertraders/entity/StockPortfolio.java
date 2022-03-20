package com.hck.exchange.supertraders.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by @hck
 */

@Data
@Entity
@Table(name = "stock_portfolio")
@NoArgsConstructor
public class StockPortfolio {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "share_id")
    private Share share;

    private Integer amount;

    public void buy(@NonNull Integer i) {
        this.amount = this.amount + i;
    }

    public void sell(@NonNull Integer i) {
        this.amount = this.amount - i;
    }

    @Override
    public String toString() {
        return "StockPortfolio{" +
                "id=" + id +
                ", share=" + share.getSymbol() +
                ", amount=" + amount +
                '}';
    }
}
