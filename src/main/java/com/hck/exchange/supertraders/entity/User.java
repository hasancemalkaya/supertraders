package com.hck.exchange.supertraders.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by @hck
 */

@Data
@Entity
@Table(name = "`user`", uniqueConstraints = @UniqueConstraint(columnNames = "userId"))
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String userId;
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Trade> trade;

    public Boolean haveRegisteredPortfolio() {
        if (this.portfolio == null) {
            return Boolean.FALSE;
        }
        return this.portfolio.getIsRegistered();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", portfolio=" + portfolio.toString() +
                '}';
    }
}
