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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.demo.config.ExchangeRateConfig;
import com.financial.demo.exception.ExchangeRateErrorException;
import com.financial.demo.model.dto.ExchangeRateResponseDTO;

@SpringBootTest
class ExchangeRateServiceTest {

    @Autowired
    public ExchangeRateService exchangeRateService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExchangeRateConfig exchangeRateConfig;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testUnableToAccessServiceTest() throws URISyntaxException {
        ExchangeRateResponseDTO response = new ExchangeRateResponseDTO();
        response.setRate(BigDecimal.ONE);
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(exchangeRateConfig.getExchangeRateUrl("EUR","USD"))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(ExchangeRateErrorException.class,() -> this.exchangeRateService.findExchangeRateForCurrencies("EUR","USD") );
        mockServer.verify();
    }

    @Test
    void testEmptyRateResponseTest() throws URISyntaxException, JsonProcessingException {
        ExchangeRateResponseDTO response = new ExchangeRateResponseDTO();
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(exchangeRateConfig.getExchangeRateUrl("EUR","USD"))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        Assertions.assertThrows(ExchangeRateErrorException.class,() -> this.exchangeRateService.findExchangeRateForCurrencies("EUR","USD") );
        mockServer.verify();
    }

    @Test
    void testSuccessResponseTest() throws URISyntaxException, JsonProcessingException {
        ExchangeRateResponseDTO response = new ExchangeRateResponseDTO();
        response.setRate(BigDecimal.ONE);
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("https://fake-url.com/exchange-rate/?sender=EUR&receiver=USD")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        ExchangeRateResponseDTO result = this.exchangeRateService.findExchangeRateForCurrencies("EUR","USD");
        mockServer.verify();
        Assertions.assertEquals(BigDecimal.ONE, result.getRate());
    }

}
