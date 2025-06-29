package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Repair;
import com.property.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repairs")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @GetMapping("/resident/{residentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENT')")
    public ResponseEntity<List<Repair>> getRepairsByResidentId(@PathVariable Integer residentId) {
        List<Repair> repairs = repairService.getRepairsByResidentId(residentId);
        return ResponseEntity.ok(repairs);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Repair>> getRepairsByStatus(@PathVariable String status) {
        List<Repair> repairs = repairService.getRepairsByStatus(status);
        return ResponseEntity.ok(repairs);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESIDENT')")
    public ResponseEntity<Repair> addRepair(@RequestBody Repair repair) {
        Repair savedRepair = repairService.addRepair(repair);
        return ResponseEntity.ok(savedRepair);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Repair> updateRepairStatus(
            @PathVariable Integer id,
            @RequestParam String status,
            @RequestParam(required = false) String staffNotes) {
        Repair updatedRepair = repairService.updateRepairStatus(id, status, staffNotes);
        return ResponseEntity.ok(updatedRepair);
    }
}