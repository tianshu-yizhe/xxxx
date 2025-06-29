package com.property.service;

import com.property.exception.ResourceNotFoundException;
import com.property.model.Facility;
import com.property.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAllByOrderByNameAsc();
    }

    public Facility addFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    public Facility updateFacilityStatus(Integer id, String status) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility", "id", id));

        facility.setStatus(status);
        return facilityRepository.save(facility);
    }

    public void deleteFacility(Integer id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility", "id", id));

        facilityRepository.delete(facility);
    }
}