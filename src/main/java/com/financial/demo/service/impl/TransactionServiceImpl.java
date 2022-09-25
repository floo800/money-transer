package com.financial.demo.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financial.demo.exception.InvalidAmountException;
import com.financial.demo.exception.NotEnoughBalanceException;
import com.financial.demo.exception.UnknownAccountException;
import com.financial.demo.model.db.AccountDB;
import com.financial.demo.model.dto.ExchangeRateResponseDTO;
import com.financial.demo.model.dto.TransactionInDTO;
import com.financial.demo.repository.AccountRepository;
import com.financial.demo.service.ExchangeRateService;
import com.financial.demo.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final ExchangeRateService exchangeRateService;

    public TransactionServiceImpl(final AccountRepository accountRepository, final ExchangeRateService exchangeRateService) {
        this.accountRepository = accountRepository;
        this.exchangeRateService = exchangeRateService;
    }


    @Override
    @Transactional
    public void transferMoney(TransactionInDTO transaction) throws UnknownAccountException{
        if(transaction.getSenderAccount() == null || transaction.getReceiverAccount() == null){
            throw new UnknownAccountException();
        }
        if(transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) < 0){
            throw  new InvalidAmountException();
        }
        AccountDB sender =this.accountRepository.findById(transaction.getSenderAccount()).orElseThrow(UnknownAccountException::new);
        AccountDB receiver = this.accountRepository.findById(transaction.getReceiverAccount()).orElseThrow(UnknownAccountException::new);

        if(sender.getBalance().compareTo(transaction.getAmount()) < 0){
            throw new NotEnoughBalanceException();
        }

        ExchangeRateResponseDTO rate = exchangeRateService.findExchangeRateForCurrencies(sender.getCurrency(),receiver.getCurrency());
        sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount().multiply(rate.getRate())));

        this.accountRepository.save(sender);
        this.accountRepository.save(receiver);
    }


}
