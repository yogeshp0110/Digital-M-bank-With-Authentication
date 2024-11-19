package com.maveric.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.maveric.bank.entity.Account;
import com.maveric.bank.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public List<Account> getAllAccounts() {
		return accountService.getAllAccounts();
	}

	@GetMapping("/{accountId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','CUSTOMER')")
	public ResponseEntity<Account> getAccountById(@PathVariable String accountId) {
		Account account = accountService.getAccountById(accountId);
		return new ResponseEntity<>(account, HttpStatus.OK);
	}

	
	@DeleteMapping("/{accountId}")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
		accountService.deleteAccount(accountId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
