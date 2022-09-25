package com.financial.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financial.demo.model.dto.ExchangeRateResponseDTO;
import com.financial.demo.service.ExchangeRateService;


@RestController
@RequestMapping("api/v1/exchange-rate")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(final ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping()
    public ExchangeRateResponseDTO findExchangeRateForCurrencies(@RequestParam(name = "current-currency") String currentCurrency,@RequestParam(name = "target-currency") String targetCurrency){
        return this.exchangeRateService.findExchangeRateForCurrencies(currentCurrency,targetCurrency);
    }
}
