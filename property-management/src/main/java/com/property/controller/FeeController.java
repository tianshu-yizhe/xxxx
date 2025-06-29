package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Fee;
import com.property.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @GetMapping("/resident/{residentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENT')")
    public ResponseEntity<List<Fee>> getFeesByResidentId(@PathVariable Integer residentId) {
        List<Fee> fees = feeService.getFeesByResidentId(residentId);
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/unpaid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Fee>> getUnpaidFees() {
        List<Fee> fees = feeService.getUnpaidFees();
        return ResponseEntity.ok(fees);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Fee> addFee(@RequestBody Fee fee) {
        Fee savedFee = feeService.addFee(fee);
        return ResponseEntity.ok(savedFee);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENT')")
    public ResponseEntity<Fee> updateFeeStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        Fee updatedFee = feeService.updateFeeStatus(id, status);
        return ResponseEntity.ok(updatedFee);
    }
}