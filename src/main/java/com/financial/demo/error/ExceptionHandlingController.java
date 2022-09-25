package com.financial.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.financial.demo.exception.ExchangeRateErrorException;
import com.financial.demo.exception.InvalidAmountException;
import com.financial.demo.exception.NotEnoughBalanceException;
import com.financial.demo.exception.UnknownAccountException;

@RestControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(ExchangeRateErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestError handleUnknownAccountException(final ExchangeRateErrorException exception) {
        return new RestError(RestError.Code.EXCHANGE_RATE, exception.getMessage());
    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError handleInvalidTransactionException(final InvalidAmountException exception) {
        return new RestError(RestError.Code.INVALID_AMOUNT, exception.getMessage());
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError handleConstraintViolationException(final NotEnoughBalanceException exception) {
        return new RestError(RestError.Code.NOT_ENOUGH_BALANCE, exception.getMessage());
    }

    @ExceptionHandler(UnknownAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError handleConstraintViolationException(final UnknownAccountException exception) {
        return new RestError(RestError.Code.UNKNOWN_ACCOUNT, exception.getMessage());
    }
}
