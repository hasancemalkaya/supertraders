package com.hck.exchange.supertraders.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by @hck
 */

@Data
@Entity
@Table(name = "share", uniqueConstraints = @UniqueConstraint(columnNames = "symbol"))
@NoArgsConstructor
public class Share {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, length = 3)
    @Length(min = 3, max = 3)
    private String symbol;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "share")
    @LazyCollection(LazyCollectionOption.TRUE)
    private ShareUpdate shareUpdate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "share")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<StockPortfolio> stockPortfolio;

    @OneToMany(mappedBy = "share")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Trade> trade;

    private BigDecimal rate;
    private Boolean isRegistered;

    @PreUpdate
    @PrePersist
    public void preCase() {
        this.symbol = this.symbol.trim().toUpperCase();
        this.rate = this.rate.setScale(2, RoundingMode.HALF_UP);
    }

    public Optional<ShareUpdate> getShareUpdate() {
        return Optional.ofNullable(this.shareUpdate);
    }

    @Override
    public String toString() {
        return "Share{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", rate=" + rate +
                ", isRegistered=" + isRegistered +
                '}';
    }
}
