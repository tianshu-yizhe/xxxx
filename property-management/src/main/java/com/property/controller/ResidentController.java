package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Resident;
import com.property.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Resident>> getAllResidents() {
        List<Resident> residents = residentService.getAllResidents();
        return ResponseEntity.ok(residents);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resident> getResidentById(@PathVariable Integer id) {
        Resident resident = residentService.getResidentById(id);
        return ResponseEntity.ok(resident);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Resident>> searchResidents(@RequestParam String keyword) {
        List<Resident> residents = residentService.searchResidents(keyword);
        return ResponseEntity.ok(residents);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resident> addResident(@RequestBody Resident resident) {
        Resident savedResident = residentService.addResident(resident);
        return ResponseEntity.ok(savedResident);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resident> updateResident(
            @PathVariable Integer id,
            @RequestBody Resident resident) {
        resident.setId(id);
        Resident updatedResident = residentService.updateResident(resident);
        return ResponseEntity.ok(updatedResident);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteResident(@PathVariable Integer id) {
        residentService.deleteResident(id);
        return ResponseEntity.ok(new ApiResponse(true, "住户删除成功"));
    }
}