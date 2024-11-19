package com.maveric.bank.service;

import java.util.List;
import java.util.Optional;

import com.maveric.bank.dto.CustomerDTO;
import com.maveric.bank.entity.Customer;

public interface ICustomerService {
	String createCustomer(CustomerDTO customerDTO); 
    List<Customer> getAllCustomers();                 
    Optional<Customer> getCustomerById(Long customerId);
    String deleteCustomer(Long customerId);      
}
