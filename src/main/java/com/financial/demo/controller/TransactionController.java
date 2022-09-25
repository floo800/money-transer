package com.financial.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.financial.demo.model.dto.TransactionInDTO;
import com.financial.demo.service.TransactionService;

@RestController("api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping()
    public void transferMoney(@RequestBody TransactionInDTO transaction){
        this.transactionService.transferMoney(transaction);
    }
}
