package com.maveric.bank.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private Double issuedAmount;
    private Double remainingAmount; // This will be calculated
    private String accountId;
    private String branchId;
    private LocalDate sanctionDate;
    private Integer sanctionPeriod; // In months
    private Double interestRate;
    private Double emi;

    @Column(name = "monthly_interest_amount")
    private Double monthlyInterestAmount; // Add this line to store monthly interest amount
}
