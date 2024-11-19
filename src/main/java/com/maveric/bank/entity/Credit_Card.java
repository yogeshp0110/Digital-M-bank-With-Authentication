package com.maveric.bank.entity;

import java.time.LocalDate;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit_Card {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "credit_card_id")
	    private Long creditCardId;

	    @Column(name = "expiry_date", nullable = false)
	    private LocalDate expiryDate;

	    @Column(name = "card_limit", nullable = false)
	    private Double cardLimit;

	    @Column(name = "credit_card_no", nullable = false, unique = true) // Add this line for the new field
	    private String creditCardNo;
	    
	    
	    @ManyToOne	   
	    @JoinColumn(name = "account_id", nullable = false)
	    private Account account;

	    
	    @ManyToOne
	    @JoinColumn(name = "customer_id", nullable = false)  // Foreign key to the Customer table
	    private Customer customer;


}
