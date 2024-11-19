package com.maveric.bank.dto;

import com.maveric.bank.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {
    private Long loanId;
    private Double issuedAmount;
    private Double remainingAmount; // This will be calculated
    private String accountId;
    private String branchId;
    private LocalDate sanctionDate;
    private Integer sanctionPeriod; // In months
    private Double interestRate;
    private Double monthlyInterestAmount; // New field for monthly interest amount
    private Double emi;

    // Convert LoanDTO to Loan entity
    public Loan toEntity() {
        Loan loan = new Loan();
        loan.setIssuedAmount(this.issuedAmount);
        loan.setSanctionDate(this.sanctionDate);
        loan.setSanctionPeriod(this.sanctionPeriod);
        loan.setInterestRate(this.interestRate);
        loan.setAccountId(this.accountId);
        loan.setBranchId(this.branchId);
        return loan;
    }
}
