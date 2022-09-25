package com.financial.demo.exception;

public class UnknownAccountException extends RuntimeException{
    public UnknownAccountException(){
        super();
    }

    public UnknownAccountException(final String message){
        super(message);
    }

    public UnknownAccountException(final String message, final Throwable cause) {
        super(message,cause);
    }
}
