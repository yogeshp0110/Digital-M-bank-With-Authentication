package com.maveric.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maveric.bank.entity.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {

	Optional<Branch> findTopByOrderByBranchIdDesc();
}
