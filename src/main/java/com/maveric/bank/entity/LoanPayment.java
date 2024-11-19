package com.maveric.bank.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "loan_payments")
public class LoanPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanPaymentId; // Unique identifier for the loan payment

    private Long loanId; // Associated loan ID
    private Double monthlyInterestAmount; // Monthly interest amount
    private String accountId; // Account ID
    private String branchId; // Branch ID
}
