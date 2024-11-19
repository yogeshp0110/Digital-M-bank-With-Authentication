package com.maveric.bank.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maveric.bank.entity.Banker;
import com.maveric.bank.entity.Branch;
import com.maveric.bank.exception.ResourceNotFoundException;
import com.maveric.bank.repository.BankerRepository;
import com.maveric.bank.repository.BranchRepository;

@Service
public class BankerService {

	@Autowired
	private BankerRepository bankerRepository;

	@Autowired
	private BranchRepository branchRepository;

	public Banker createBanker(Banker banker) {
		if (banker == null) {
			throw new IllegalArgumentException("Banker object cannot be null");
		}

		if (banker.getBranch() == null || banker.getBranch().getBranchId() == null) {
			throw new IllegalArgumentException("Branch information must be provided for the Banker");
		}

		Branch branch = branchRepository.findById(banker.getBranch().getBranchId()).orElseThrow(
				() -> new ResourceNotFoundException("Branch not found with ID: " + banker.getBranch().getBranchId()));

		if (banker.getBankerName() == null || banker.getBankerName().trim().isEmpty()) {
			throw new IllegalArgumentException("Banker name cannot be null or empty");
		}

		banker.setBranch(branch);
		banker.setBankerId(generateUniqueBankerId());
		return bankerRepository.save(banker);
	}

	private Long generateUniqueBankerId() {
		Long maxId = bankerRepository.findMaxBankerId();
		if (maxId == null || maxId < 100201) {
			return 100201L;
		}
		return maxId + 1;
	}

	public List<Banker> getAllBankers() {
		List<Banker> bankers = bankerRepository.findAll();
		if (bankers.isEmpty()) {
			throw new ResourceNotFoundException("No bankers found");
		}
		return bankers;
	}

	public Banker getBankerById(Long bankerId) {
		return bankerRepository.findById(bankerId)
				.orElseThrow(() -> new ResourceNotFoundException("Banker not found with ID: " + bankerId));
	}

	public void deleteBanker(Long bankerId) {
		Banker banker = bankerRepository.findById(bankerId)
				.orElseThrow(() -> new ResourceNotFoundException("Banker not found with ID: " + bankerId));

		bankerRepository.delete(banker);
	}
}
