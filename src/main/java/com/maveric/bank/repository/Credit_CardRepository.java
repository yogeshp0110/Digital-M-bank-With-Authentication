package com.maveric.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maveric.bank.entity.Credit_Card;

@Repository
public interface Credit_CardRepository extends JpaRepository<Credit_Card, Long> {

}
