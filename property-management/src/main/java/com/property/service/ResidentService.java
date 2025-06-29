package com.property.service;

import com.property.exception.ResourceNotFoundException;
import com.property.model.Resident;
import com.property.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<Resident> getAllResidents() {
        return residentRepository.findAllByOrderByRoomNumberAsc();
    }

    public Resident getResidentById(Integer id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resident", "id", id));
    }

    public List<Resident> searchResidents(String keyword) {
        return residentRepository.searchByNameOrRoomNumberOrPhone(keyword);
    }

    public Resident addResident(Resident resident) {
        return residentRepository.save(resident);
    }

    public Resident updateResident(Resident resident) {
        Resident existingResident = residentRepository.findById(resident.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Resident", "id", resident.getId()));

        existingResident.setName(resident.getName());
        existingResident.setPhone(resident.getPhone());
        existingResident.setRoomNumber(resident.getRoomNumber());
        existingResident.setIdentityNumber(resident.getIdentityNumber());

        return residentRepository.save(existingResident);
    }

    public void deleteResident(Integer id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resident", "id", id));

        residentRepository.delete(resident);
    }
}