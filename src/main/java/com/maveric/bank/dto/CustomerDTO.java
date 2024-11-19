package com.maveric.bank.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CustomerDTO {
	private Long customerId;
	private String customerName;
	private LocalDate dob;
	private String MobileNo;
	private AccountDTO account;

  
}
