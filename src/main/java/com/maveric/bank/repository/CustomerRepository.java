package com.maveric.bank.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maveric.bank.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	  Optional<Customer> findTopByOrderByCustomerIdDesc();

			@Query("SELECT COALESCE(MAX(c.customerId), 0) FROM Customer c")
		    Long findMaxBankerId();
}
