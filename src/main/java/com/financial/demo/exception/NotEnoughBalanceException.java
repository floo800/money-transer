package com.financial.demo.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(){
        super();
    }

    public NotEnoughBalanceException(final String message){
        super(message);
    }

    public NotEnoughBalanceException(final String message, final Throwable cause) {
        super(message,cause);
    }
}
