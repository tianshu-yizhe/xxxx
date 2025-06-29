package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Facility;
import com.property.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {
        List<Facility> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok(facilities);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Facility> addFacility(@RequestBody Facility facility) {
        Facility savedFacility = facilityService.addFacility(facility);
        return ResponseEntity.ok(savedFacility);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Facility> updateFacilityStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        Facility updatedFacility = facilityService.updateFacilityStatus(id, status);
        return ResponseEntity.ok(updatedFacility);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteFacility(@PathVariable Integer id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.ok(new ApiResponse(true, "设施删除成功"));
    }
}