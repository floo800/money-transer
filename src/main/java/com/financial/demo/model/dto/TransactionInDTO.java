package com.financial.demo.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionInDTO {
    Long senderAccount;
    Long receiverAccount;
    BigDecimal amount;
}
