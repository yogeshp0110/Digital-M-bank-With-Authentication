package com.maveric.bank.service;

import com.maveric.bank.dto.LoanPaymentDTO;
import com.maveric.bank.entity.LoanPayment;
import com.maveric.bank.repository.LoanPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanPaymentService {

    @Autowired
    private LoanPaymentRepository loanPaymentRepository;

    // Create a new loan payment
    public LoanPaymentDTO createLoanPayment(LoanPaymentDTO loanPaymentDTO) {
        LoanPayment loanPayment = loanPaymentDTO.toEntity(); // Convert DTO to Entity
        LoanPayment savedLoanPayment = loanPaymentRepository.save(loanPayment); // Save loan payment
        return convertToDTO(savedLoanPayment); // Return the saved DTO
    }

    // Get all loan payments by loan ID
    public List<LoanPaymentDTO> getLoanPaymentsByLoanId(Long loanId) {
        List<LoanPayment> payments = loanPaymentRepository.findByLoanId(loanId);
        return payments.stream().map(this::convertToDTO).collect(Collectors.toList()); // Convert to DTOs
    }

    // Convert LoanPayment entity to LoanPaymentDTO
    private LoanPaymentDTO convertToDTO(LoanPayment loanPayment) {
        LoanPaymentDTO loanPaymentDTO = new LoanPaymentDTO();
        loanPaymentDTO.setLoanPaymentId(loanPayment.getLoanPaymentId());
        loanPaymentDTO.setLoanId(loanPayment.getLoanId());
        loanPaymentDTO.setMonthlyInterestAmount(loanPayment.getMonthlyInterestAmount());
        loanPaymentDTO.setAccountId(loanPayment.getAccountId());
        loanPaymentDTO.setBranchId(loanPayment.getBranchId());
        return loanPaymentDTO; // Return the DTO
    }
}
