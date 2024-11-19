package com.maveric.bank.service;

import com.maveric.bank.dto.LoanDTO;
import com.maveric.bank.entity.Loan;
import com.maveric.bank.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    // Create a new loan
    public LoanDTO createLoan(LoanDTO loanDTO) {
        Loan loan = loanDTO.toEntity(); // Convert DTO to Entity

        // Calculate remaining amount and monthly interest amount
        double remainingAmount = calculateRemainingAmount(loanDTO.getIssuedAmount(), loanDTO.getInterestRate(), loanDTO.getSanctionPeriod());
        double monthlyInterestAmount = calculateMonthlyInterest(loanDTO.getIssuedAmount(), loanDTO.getInterestRate());
        double emi = calculateEMI(loanDTO.getIssuedAmount(), loanDTO.getInterestRate(), loanDTO.getSanctionPeriod());

        loan.setRemainingAmount(remainingAmount);
        loan.setMonthlyInterestAmount(monthlyInterestAmount); // Set the monthly interest amount
        loan.setEmi(emi); // Set the EMI

        Loan savedLoan = loanRepository.save(loan); // Save the loan to the database

        // Update DTO with calculated values
        loanDTO.setLoanId(savedLoan.getLoanId());
        loanDTO.setRemainingAmount(savedLoan.getRemainingAmount());
        loanDTO.setMonthlyInterestAmount(savedLoan.getMonthlyInterestAmount()); // Set from saved loan
        loanDTO.setEmi(savedLoan.getEmi()); // Set from saved loan

        return loanDTO; // Return the updated DTO
    }

    // Get a loan by ID
    public LoanDTO getLoanById(Long id) {
        Optional<Loan> loan = loanRepository.findById(id);
        return loan.map(this::convertToDTO).orElse(null); // Convert to DTO if present
    }

    // Get all loans
    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream().map(this::convertToDTO).collect(Collectors.toList()); // Convert list of loans to DTOs
    }

    // Update an existing loan
    public LoanDTO updateLoan(Long id, LoanDTO loanDTO) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (optionalLoan.isPresent()) {
            Loan existingLoan = optionalLoan.get();
            existingLoan.setIssuedAmount(loanDTO.getIssuedAmount());
            existingLoan.setAccountId(loanDTO.getAccountId());
            existingLoan.setBranchId(loanDTO.getBranchId());
            existingLoan.setSanctionDate(loanDTO.getSanctionDate());
            existingLoan.setSanctionPeriod(loanDTO.getSanctionPeriod());
            existingLoan.setInterestRate(loanDTO.getInterestRate());

            // Recalculate remaining amount, monthly interest amount, and EMI
            existingLoan.setRemainingAmount(calculateRemainingAmount(loanDTO.getIssuedAmount(), loanDTO.getInterestRate(), loanDTO.getSanctionPeriod()));
            existingLoan.setMonthlyInterestAmount(calculateMonthlyInterest(loanDTO.getIssuedAmount(), loanDTO.getInterestRate())); // Set the monthly interest amount
            existingLoan.setEmi(calculateEMI(loanDTO.getIssuedAmount(), loanDTO.getInterestRate(), loanDTO.getSanctionPeriod())); // Set the EMI

            Loan updatedLoan = loanRepository.save(existingLoan); // Save the updated loan
            return convertToDTO(updatedLoan); // Return the updated DTO
        }
        return null; // Loan not found
    }

    // Delete a loan by ID
    public boolean deleteLoan(Long id) {
        if (loanRepository.existsById(id)) {
            loanRepository.deleteById(id);
            return true; // Deletion successful
        }
        return false; // Loan not found
    }

    // Calculate remaining amount after adding interest
    private Double calculateRemainingAmount(Double issuedAmount, Double interestRate, Integer sanctionPeriod) {
        // Total interest for the entire period
        double totalInterest = issuedAmount * interestRate / 100 * (sanctionPeriod / 12.0);
        // Total amount = Principal + Interest
        return issuedAmount + totalInterest;
    }

    // Calculate monthly interest amount
    private Double calculateMonthlyInterest(Double issuedAmount, Double interestRate) {
        return (issuedAmount * interestRate / 100) / 12.0; // Monthly interest calculation
    }

    // Calculate EMI and add interestRate to EMI
    private Double calculateEMI(Double issuedAmount, Double interestRate, Integer sanctionPeriod) {
        double monthlyInterestRate = interestRate / (12 * 100); // Monthly interest rate
        if (monthlyInterestRate == 0) {
            return issuedAmount / sanctionPeriod; // Handle zero interest case
        }
        double emi = (issuedAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, sanctionPeriod)) / 
                     (Math.pow(1 + monthlyInterestRate, sanctionPeriod) - 1);
        
        // Add interest rate to the EMI
        return emi + interestRate;
    }

    // Convert Loan entity to LoanDTO
    private LoanDTO convertToDTO(Loan loan) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanId(loan.getLoanId());
        loanDTO.setIssuedAmount(loan.getIssuedAmount());
        loanDTO.setRemainingAmount(loan.getRemainingAmount());
        loanDTO.setAccountId(loan.getAccountId());
        loanDTO.setBranchId(loan.getBranchId());
        loanDTO.setSanctionDate(loan.getSanctionDate());
        loanDTO.setSanctionPeriod(loan.getSanctionPeriod());
        loanDTO.setInterestRate(loan.getInterestRate());
        loanDTO.setMonthlyInterestAmount(loan.getMonthlyInterestAmount()); // Get from loan entity
        loanDTO.setEmi(loan.getEmi()); // Get from loan entity
        return loanDTO; // Return the DTO
    }
}
