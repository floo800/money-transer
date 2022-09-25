package com.financial.demo.exception;

public class ExchangeRateErrorException extends RuntimeException{
    public ExchangeRateErrorException(){
        super();
    }

    public ExchangeRateErrorException(final String message){
        super(message);
    }

    public ExchangeRateErrorException(final String message, final Throwable cause) {
        super(message,cause);
    }
}
