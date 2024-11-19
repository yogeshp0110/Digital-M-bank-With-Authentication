package com.maveric.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.entity.Banker;
import com.maveric.bank.exception.ResourceNotFoundException;
import com.maveric.bank.service.BankerService;

@RestController
@RequestMapping("/bankers")
public class BankerController {

	@Autowired
	private BankerService bankerService;
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<Banker> createBanker(@RequestBody Banker banker) {
		Banker savedBanker = bankerService.createBanker(banker);
		return ResponseEntity.ok(savedBanker);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('SUPERADMIN')")
	public List<Banker> getAllBankers() {
		return bankerService.getAllBankers();
	}
	
	@GetMapping("/{bankerId}")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<Banker> getBankerById(@PathVariable Long bankerId) {
		try {
			Banker banker = bankerService.getBankerById(bankerId);
			return new ResponseEntity<>(banker, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{bankerId}")
	 @PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<String> deleteBanker(@PathVariable Long bankerId) {
		try {
			bankerService.deleteBanker(bankerId);
			return new ResponseEntity<>("Banker successfully deleted", HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>("Banker not found with ID: " + bankerId, HttpStatus.NOT_FOUND);
		}
	}

}
