package com.financial.demo.service;

import com.financial.demo.model.dto.ExchangeRateResponseDTO;

public interface ExchangeRateService {

    /**
     * @param senderCurrency currency of the sender
     * @param receiverCurrency currency of the receiver
     * @return the exchange rate between the two currencies
     */
    ExchangeRateResponseDTO findExchangeRateForCurrencies(String senderCurrency,String receiverCurrency);
}
