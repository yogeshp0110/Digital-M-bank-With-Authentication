package com.maveric.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.maveric.bank.dto.CustomerDTO;
import com.maveric.bank.entity.Customer;
import com.maveric.bank.service.ICustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

   
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customerDTO) {
        String successMessage = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','CUSTOMER')")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','CUSTOMER')")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // SUPERADMIN and ADMIN can update customer details
//    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
//    @PutMapping("/{customerId}")
//    public ResponseEntity<String> updateCustomer(@PathVariable Long customerId, 
//                                                 @RequestBody CustomerDTO customerDTO) {
//        customerService.updateCustomer(customerId, customerDTO);
//        return new ResponseEntity<>("Customer updated successfully", HttpStatus.OK);
//    }

    // Only SUPERADMIN can delete a customer
    
    
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>("Customer deleted successfully", HttpStatus.NO_CONTENT);
    }
}
