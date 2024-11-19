package com.maveric.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maveric.bank.entity.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
