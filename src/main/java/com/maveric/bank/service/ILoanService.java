package com.maveric.bank.service;

import java.util.List;
import java.util.Optional;

import com.maveric.bank.dto.LoanDTO;
import com.maveric.bank.entity.Loan;

public interface ILoanService {

	Loan createLoan(LoanDTO loanDTO, String accountId);

	List<Loan> getAllLoans();

	Optional<Loan> getLoanById(String loanId);

	void deleteLoan(String loanId);

}
