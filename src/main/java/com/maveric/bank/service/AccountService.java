package com.maveric.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.maveric.bank.dto.AccountDTO;
import com.maveric.bank.entity.Account;
import com.maveric.bank.entity.Branch;
import com.maveric.bank.repository.AccountRepository;

import java.util.List;
import java.util.Random;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BranchService branchService;

	public Account createAccount(AccountDTO accountDTO) {
		if (accountDTO.getBranchId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch ID cannot be null");
		}
		Branch branch = branchService.getBranchById(accountDTO.getBranchId());

		if (accountDTO.getAccountType() == null || accountDTO.getAccountType().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account type cannot be null or empty");
		}

		Account account = new Account();
		account.setAccountBalance(accountDTO.getAccountBalance());
		account.setAccountType(accountDTO.getAccountType());
		account.setBranch(branch);

		account.setAccountId(generateRandomAccountId());

		try {
			Account savedAccount = accountRepository.save(account);
			return savedAccount;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An unexpected error occurred while creating the account", e);
		}
	}

	public String generateRandomAccountId() {
		Random random = new Random();
		long accountId = 100000000000L + random.nextInt(900000000);
		return String.valueOf(accountId);
	}

	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

	public Account getAccountById(String accountId) {
		return accountRepository.findById(accountId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with ID: " + accountId));
	}

	public void deleteAccount(String accountId) {
		accountRepository.deleteById(accountId);
	}
}
