package com.hck.exchange.supertraders.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by @hck
 */

@Data
@Entity
@Table(name = "portfolio")
@NoArgsConstructor
public class Portfolio implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "portfolio")
    private List<StockPortfolio> stockPortfolio;

    private Boolean isRegistered;

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", stockPortfolio=" + stockPortfolio +
                ", isRegistered=" + isRegistered +
                '}';
    }

    public void removeStockPortfolio(StockPortfolio stockPortfolio) {
        if (this.stockPortfolio != null && !this.stockPortfolio.isEmpty() && this.stockPortfolio.contains(stockPortfolio)) {
            this.stockPortfolio.remove(stockPortfolio);
        }
    }
}
