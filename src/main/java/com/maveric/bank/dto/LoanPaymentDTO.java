package com.maveric.bank.dto;

import com.maveric.bank.entity.LoanPayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanPaymentDTO {
    private Long loanPaymentId; // Unique identifier for the loan payment
    private Long loanId; // Associated loan ID
    private Double monthlyInterestAmount; // Monthly interest amount
    private String accountId; // Account ID
    private String branchId; // Branch ID

    // Convert LoanPaymentDTO to LoanPayment entity
    public LoanPayment toEntity() {
        LoanPayment loanPayment = new LoanPayment();
        loanPayment.setLoanId(this.loanId);
        loanPayment.setMonthlyInterestAmount(this.monthlyInterestAmount);
        loanPayment.setAccountId(this.accountId);
        loanPayment.setBranchId(this.branchId);
        return loanPayment;
    }
}
