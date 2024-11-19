package com.maveric.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
	private String accountId;
	private double accountBalance;
	private String accountType;
	private String branchId;
    
}
