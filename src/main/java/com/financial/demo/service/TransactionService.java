package com.financial.demo.service;

import org.springframework.transaction.annotation.Transactional;

import com.financial.demo.model.dto.TransactionInDTO;

public interface TransactionService {
    /**
     * Creation of a transaction between to account
     * @param transaction data to process transation
     */
    @Transactional(readOnly = true) void transferMoney(TransactionInDTO transaction);
}
