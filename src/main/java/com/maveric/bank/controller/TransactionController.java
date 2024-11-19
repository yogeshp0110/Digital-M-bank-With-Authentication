package com.maveric.bank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.dto.TransactionDTO;
import com.maveric.bank.entity.Transaction;
import com.maveric.bank.service.TransactionService;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    
    @PostMapping(value = "/credit",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','CUSTOMER')")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        TransactionDTO transactionDTO = transactionService.convertToDTO(createdTransaction);
        return ResponseEntity.ok(transactionDTO);
        
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            String errorMessage = "Transaction with ID: " + id + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }
    
    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Map<String, Object> response = new HashMap<>();
        if (transactions.isEmpty()) {
            response.put("message", "No transactions found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("message", transactions.size() + " transactions retrieved successfully.");
        response.put("transactions", transactions);
        return ResponseEntity.ok(response);
    }

    
   
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
    	 boolean isDeleted = transactionService.deleteTransaction(id); 
    	    if (isDeleted) {
    	        String message = "Transaction with ID: " + id + " has been successfully deleted.";
    	        return ResponseEntity.ok(message);
    	    } else {
    	        String errorMessage = "Transaction with ID: " + id + " not found.";
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    	    }
    }
    
    
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        updatedTransaction.setTransactionId(id);  // Set the ID to ensure update happens on the correct entity
        Transaction savedTransaction = transactionService.updateTransaction(id, updatedTransaction);
        TransactionDTO transactionDTO = transactionService.convertToDTO(savedTransaction);
        transactionDTO.setMessage("Transaction successfully updated with transaction ID: " + savedTransaction.getTransactionId());
        return ResponseEntity.ok(transactionDTO);
    }
}

