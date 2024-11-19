package com.maveric.bank.controller;

import com.maveric.bank.dto.LoanPaymentDTO;
import com.maveric.bank.service.LoanPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan-payments")
public class LoanPaymentController {

    @Autowired
    private LoanPaymentService loanPaymentService;
  
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','CUSTOMER')")
    public ResponseEntity<LoanPaymentDTO> createLoanPayment(@RequestBody LoanPaymentDTO loanPaymentDTO) {
        LoanPaymentDTO createdLoanPayment = loanPaymentService.createLoanPayment(loanPaymentDTO);
        return new ResponseEntity<>(createdLoanPayment, HttpStatus.CREATED); // Return created response
    }

    @GetMapping("/loan/{loanId}")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','CUSTOMER')")
    public ResponseEntity<List<LoanPaymentDTO>> getLoanPaymentsByLoanId(@PathVariable Long loanId) {
        List<LoanPaymentDTO> loanPayments = loanPaymentService.getLoanPaymentsByLoanId(loanId);
        return new ResponseEntity<>(loanPayments, HttpStatus.OK); // Return loan payments
    }
}
