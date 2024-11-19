package com.maveric.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maveric.bank.entity.Branch;
import com.maveric.bank.exception.ResourceNotFoundException;
import com.maveric.bank.repository.BranchRepository;

@Service
public class BranchService {

	@Autowired
	private BranchRepository branchRepository;

	public Branch createBranch(Branch branch) {
		branch.setBranchId(generateBranchId());
		return branchRepository.save(branch);
	}

	private String generateBranchId() {
		Optional<Branch> lastBranch = branchRepository.findTopByOrderByBranchIdDesc();
		String newId;

		if (lastBranch.isPresent()) {
			String lastId = lastBranch.get().getBranchId();
			int numericPart = Integer.parseInt(lastId.substring(3)); // Extract the numeric part
			newId = String.format("BR%04d", numericPart + 1); // Generate new ID with a four-digit number
		} else {
			newId = "BR0001";
		}
		return newId;
	}

	public List<Branch> getAllBranches() {
		return branchRepository.findAll();
	}

	public Branch getBranchById(String branchId) {
		return branchRepository.findById(branchId)
				.orElseThrow(() -> new ResourceNotFoundException("Branch not found with ID: " + branchId));
	}

	public String deleteBranch(String branchId) {
	    if (!branchRepository.existsById(branchId)) {
	        throw new ResourceNotFoundException("Branch not found with ID: " + branchId);
	    }
	    branchRepository.deleteById(branchId);
	    return "Branch successfully deleted with ID: " + branchId;  // Return success message
	}


}
