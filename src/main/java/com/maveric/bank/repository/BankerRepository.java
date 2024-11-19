package com.maveric.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maveric.bank.entity.Banker;

@Repository
public interface BankerRepository extends JpaRepository<Banker, Long> {
	
	@Query("SELECT COALESCE(MAX(b.bankerId), 0) FROM Banker b")
	    Long findMaxBankerId();
}
