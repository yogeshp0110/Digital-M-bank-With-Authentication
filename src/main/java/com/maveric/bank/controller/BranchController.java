package com.maveric.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.entity.Branch;
import com.maveric.bank.service.BranchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/branches")
@Tag(name = "Branch", description = "The Branch Management API")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        Branch createdBranch = branchService.createBranch(branch);
        return ResponseEntity.ok(createdBranch);
    }

// 	  SUPERADMIN: Can update branches
//    @PutMapping("/{branchId}")
//    @PreAuthorize("hasRole('SUPERADMIN')")
//    public ResponseEntity<Branch> updateBranch(@PathVariable String branchId, @RequestBody Branch branch) {
//        Branch updatedBranch = branchService.updateBranch(branchId, branch);
//        return ResponseEntity.ok(updatedBranch);
//    }

   
    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> deleteBranch(@PathVariable String branchId) {
        String message = branchService.deleteBranch(branchId); 
        return ResponseEntity.ok(message);  
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Get all branches", description = "Fetch all branches from the system")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{branchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<Branch> getBranchById(@PathVariable String branchId) {
        Branch branch = branchService.getBranchById(branchId);
        return ResponseEntity.ok(branch);
    }
}
