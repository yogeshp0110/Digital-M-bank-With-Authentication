package com.maveric.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maveric.bank.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
