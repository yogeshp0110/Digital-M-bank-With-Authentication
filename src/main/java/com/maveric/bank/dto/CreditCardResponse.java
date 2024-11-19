package com.maveric.bank.dto;

import java.time.LocalDate;

import com.maveric.bank.entity.Credit_Card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardResponse {
	private String message;
    private Long creditCardId;
    private LocalDate expiryDate;
    private Double cardLimit;
    private String accountId;
    private Long customerId;
    
    
    public CreditCardResponse(String message, Credit_Card creditCard) {
        this.message = message;
        this.creditCardId = creditCard.getCreditCardId();
        this.expiryDate = creditCard.getExpiryDate();
        this.cardLimit = creditCard.getCardLimit();
        this.accountId = creditCard.getAccount().getAccountId();
        this.customerId = creditCard.getCustomer().getCustomerId();
    }

}
