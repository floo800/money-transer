package com.financial.demo.model.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "e_account")
@Table(name = "e_account")
@Getter
@Setter
@NoArgsConstructor
public class AccountDB {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private BigDecimal balance;

    public AccountDB(final String currency, final BigDecimal balance) {
        this.currency = currency;
        this.balance = balance;
    }
}
