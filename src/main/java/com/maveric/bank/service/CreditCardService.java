package com.maveric.bank.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.maveric.bank.entity.Account;
import com.maveric.bank.entity.Credit_Card;
import com.maveric.bank.entity.Customer;
import com.maveric.bank.exception.ResourceNotFoundException;
import com.maveric.bank.repository.Credit_CardRepository;

@Service
public class CreditCardService {

    @Autowired
    private Credit_CardRepository creditCardRepository;

   

    public Credit_Card createCreditCard(Credit_Card creditCard) {
    	if (creditCard.getCreditCardNo() == null || creditCard.getCreditCardNo().isEmpty()) {
            throw new IllegalArgumentException("Credit card number cannot be null or empty");
        }

        if (creditCard.getExpiryDate() == null) {
            throw new IllegalArgumentException("Expiry date cannot be null");
        }

        if (creditCard.getCardLimit() == null || creditCard.getCardLimit() <= 0) {
            throw new IllegalArgumentException("Card limit must be greater than zero");
        }

        if (creditCard.getAccount() == null || creditCard.getAccount().getAccountId() == null) {
            throw new IllegalArgumentException("Account information is required");
        }

        if (creditCard.getCustomer() == null || creditCard.getCustomer().getCustomerId() == null) {
            throw new IllegalArgumentException("Customer information is required");
        }


        try {
            // Save the credit card to the database
            return creditCardRepository.save(creditCard);
        } catch (DataIntegrityViolationException e) {
            // Catch any integrity violations (like unique constraints)
            throw new DataIntegrityViolationException("Credit card data violates database constraints: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected errors
            throw new RuntimeException("An error occurred while saving the credit card: " + e.getMessage());
        }
    }

    public Credit_Card getCreditCardById(Long id) {
        return creditCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CreditCard not found with id: " + id));
    }

    public List<Credit_Card> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

  

 
    
    public void deleteCreditCardById(Long accountId) {
    	creditCardRepository.deleteById(accountId);
    }
}