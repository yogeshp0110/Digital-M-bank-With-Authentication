package com.maveric.bank.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.dto.CreditCardResponse;
import com.maveric.bank.entity.Credit_Card;
import com.maveric.bank.exception.ResourceNotFoundException;
import com.maveric.bank.repository.Credit_CardRepository;
import com.maveric.bank.service.CreditCardService;





@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardServices;
    
    @Autowired
    private Credit_CardRepository creditCardRepository;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','CUSTOMER')")
    public Credit_Card getCreditCardById(@PathVariable Long id) {
    	 Credit_Card creditCard = creditCardServices.getCreditCardById(id);
    	    if (creditCard == null) {
    	        throw new ResourceNotFoundException("Credit card with ID " + id + " not found");
    	    }
    	    return creditCard;
    }

    @PostMapping(value = "/credit-cards", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    public  ResponseEntity<CreditCardResponse> createCreditCard(@RequestBody Credit_Card creditCard) {
    //    return creditCardServices.createCreditCard(creditCard);
    	 Credit_Card createdCard = creditCardServices.createCreditCard(creditCard);
    	    CreditCardResponse response = new CreditCardResponse("Credit card created successfully", createdCard);
    	    return ResponseEntity.ok(response);
    }

    @GetMapping("/credit-cards")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    public List<Credit_Card> getAllCreditCards() {
        List<Credit_Card> creditcards = creditCardServices.getAllCreditCards();
      if(creditcards.isEmpty()) {
    	  throw new NoSuchElementException("No credit cards found");
      }
      
      return creditcards;
    }
    
    @DeleteMapping("/credit-cards/{creditId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable("creditId") Long creditId) {
    	 if (!creditCardRepository.existsById(creditId)) {
             throw new ResourceNotFoundException("Credit card with ID " + creditId + " not found");
         }
        creditCardServices.deleteCreditCardById(creditId);
        String responseMessage = "Credit card with ID " + creditId + " deleted successfully";
        return ResponseEntity.ok(responseMessage);
     
    }

    
}



