package com.financial.demo.error;

import lombok.Getter;

public class RestError {
    @Getter
    private final Code code;

    @Getter
    private final String message;

    public RestError(final Code code, final String message) {
        this.code = code;
        this.message = message;
    }

    public enum Code {
        EXCHANGE_RATE , INVALID_AMOUNT,NOT_ENOUGH_BALANCE , UNKNOWN_ACCOUNT;
    }

}
