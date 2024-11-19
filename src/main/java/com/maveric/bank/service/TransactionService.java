package com.maveric.bank.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maveric.bank.dto.TransactionDTO;
import com.maveric.bank.entity.Transaction;
import com.maveric.bank.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    public TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setAccountId(transaction.getAccount().getAccountId());
        transactionDTO.setCustomerId(transaction.getCustomer().getCustomerId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());

        // Custom success message
        transactionDTO.setMessage("Transaction successfully created with transaction ID: " 
                                  + transaction.getTransactionId());
        return transactionDTO;
    }
    
    // Delete Transaction
    public boolean deleteTransaction(Long id) {
    	 Optional<Transaction> transaction = transactionRepository.findById(id);
    	    if (transaction.isPresent()) {
    	        transactionRepository.delete(transaction.get());
    	        return true;
    	    } else {
    	        return false; // Transaction not found
    	    }
    }

    
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existingTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction with ID " + id + " not found"));

        // Update the fields of the existing transaction
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setTransactionDate(updatedTransaction.getTransactionDate());
        existingTransaction.setAccount(updatedTransaction.getAccount());
        existingTransaction.setCustomer(updatedTransaction.getCustomer());

        // Save the updated transaction
        return transactionRepository.save(existingTransaction);
    }

}