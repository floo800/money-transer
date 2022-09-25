package com.financial.demo.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "exchange-rate")
@Setter
@Validated
public class ExchangeRateConfig {

    @NotNull
    private String url;

    public String getExchangeRateUrl(String senderCurrency,String receiverCurrency){
        return String.format("%s/?sender=%s&receiver=%s",url,senderCurrency,receiverCurrency);
    }
}
