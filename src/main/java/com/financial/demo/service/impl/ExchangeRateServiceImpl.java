package com.financial.demo.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.financial.demo.config.ExchangeRateConfig;
import com.financial.demo.exception.ExchangeRateErrorException;
import com.financial.demo.model.dto.ExchangeRateResponseDTO;
import com.financial.demo.service.ExchangeRateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateConfig exchangeRateConfig;
    private final RestTemplate restTemplate;

    public ExchangeRateServiceImpl(final ExchangeRateConfig exchangeRateConfig, final RestTemplate restTemplate) {
        this.exchangeRateConfig = exchangeRateConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public ExchangeRateResponseDTO findExchangeRateForCurrencies(String senderCurrency,String receiverCurrency) throws  ExchangeRateErrorException{
        if(senderCurrency == null || senderCurrency.isBlank() || receiverCurrency == null || receiverCurrency.isBlank()){
            throw new ExchangeRateErrorException("Null or blank currency");
        }
        String url = exchangeRateConfig.getExchangeRateUrl(senderCurrency,receiverCurrency);
        try{
            ExchangeRateResponseDTO rate = restTemplate.getForObject(url, ExchangeRateResponseDTO.class);
            if(rate == null || rate.getRate() == null){
                throw new ExchangeRateErrorException("No value for exchange rate");
            }
            return rate;
        }catch (RestClientException ex){
            throw new ExchangeRateErrorException("Can not retrieve exchange rate",ex);
        }
    }
}
