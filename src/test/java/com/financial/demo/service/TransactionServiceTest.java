package com.financial.demo.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.demo.exception.ExchangeRateErrorException;
import com.financial.demo.exception.NotEnoughBalanceException;
import com.financial.demo.exception.UnknownAccountException;
import com.financial.demo.model.db.AccountDB;
import com.financial.demo.model.dto.ExchangeRateResponseDTO;
import com.financial.demo.model.dto.TransactionInDTO;
import com.financial.demo.repository.AccountRepository;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService financialService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void sendEnoughMoneyTest() throws URISyntaxException, JsonProcessingException {
        ExchangeRateResponseDTO response = new ExchangeRateResponseDTO();
        response.setRate(new BigDecimal("0.9"));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("https://fake-url.com/exchange-rate/?sender=EUR&receiver=USD")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        AccountDB sender = accountRepository.save(new AccountDB("EUR", BigDecimal.valueOf(10)));
        AccountDB receiver = accountRepository.save(new AccountDB("USD", BigDecimal.valueOf(10)));

        TransactionInDTO payload = new TransactionInDTO();
        payload.setAmount(BigDecimal.valueOf(10));
        payload.setSenderAccount(sender.getId());
        payload.setReceiverAccount(receiver.getId());
        this.financialService.transferMoney(payload);

        mockServer.verify();
        Assert.isTrue(BigDecimal.ZERO.compareTo(this.accountRepository.findById(sender.getId()).orElseThrow().getBalance()) == 0,"Result must be 0");
        Assert.isTrue(BigDecimal.valueOf(19).compareTo(this.accountRepository.findById(receiver.getId()).orElseThrow().getBalance()) == 0,"Result must be 19");
    }

    @Test
    void errorWithExchangeRateTest() throws URISyntaxException {
        ExchangeRateResponseDTO response = new ExchangeRateResponseDTO();
        response.setRate(new BigDecimal("0.9"));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("https://fake-url.com/exchange-rate/?sender=EUR&receiver=USD")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.GATEWAY_TIMEOUT)
                );

        AccountDB sender = accountRepository.save(new AccountDB("EUR", BigDecimal.valueOf(10)));
        AccountDB receiver = accountRepository.save(new AccountDB("USD", BigDecimal.valueOf(10)));

        TransactionInDTO payload = new TransactionInDTO();
        payload.setAmount(BigDecimal.valueOf(10));
        payload.setSenderAccount(sender.getId());
        payload.setReceiverAccount(receiver.getId());
        Assertions.assertThrows(ExchangeRateErrorException.class,() ->  this.financialService.transferMoney(payload));
    }

    @Test
    void sendNotEnoughMoneyTest() {
        ExchangeRateResponseDTO response = new ExchangeRateResponseDTO();
        response.setRate(new BigDecimal("0.9"));

        AccountDB sender = accountRepository.save(new AccountDB("EUR", BigDecimal.valueOf(5)));
        AccountDB receiver = accountRepository.save(new AccountDB("USD", BigDecimal.valueOf(10)));

        TransactionInDTO payload = new TransactionInDTO();
        payload.setAmount(BigDecimal.valueOf(10));
        payload.setSenderAccount(sender.getId());
        payload.setReceiverAccount(receiver.getId());

        Assertions.assertThrows(NotEnoughBalanceException.class,() -> this.financialService.transferMoney(payload));
    }

    @Test
    void sendAccountNotValidTest() {
        ExchangeRateResponseDTO response = new ExchangeRateResponseDTO();
        response.setRate(new BigDecimal("0.9"));

        AccountDB receiver = accountRepository.save(new AccountDB("USD", BigDecimal.valueOf(10)));

        TransactionInDTO payload = new TransactionInDTO();
        payload.setAmount(BigDecimal.valueOf(10));
        payload.setSenderAccount(-10L);
        payload.setReceiverAccount(receiver.getId());

        Assertions.assertThrows(UnknownAccountException.class,() -> this.financialService.transferMoney(payload));
    }

}
