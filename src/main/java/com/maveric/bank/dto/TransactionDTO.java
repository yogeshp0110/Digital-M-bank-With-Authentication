package com.maveric.bank.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

	private Long transactionId;
    private String accountId;
    private Long customerId;
    private Double amount;
    private LocalDateTime transactionDate;
    private String message;
	
}
