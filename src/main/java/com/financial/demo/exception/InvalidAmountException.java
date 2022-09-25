package com.financial.demo.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(){
        super();
    }

    public InvalidAmountException(final String message){
        super(message);
    }

    public InvalidAmountException(final String message, final Throwable cause) {
        super(message,cause);
    }
}
