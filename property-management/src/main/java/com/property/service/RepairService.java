package com.property.service;

import com.property.exception.ResourceNotFoundException;
import com.property.model.Repair;
import com.property.repository.RepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RepairService {

    @Autowired
    private RepairRepository repairRepository;

    public List<Repair> getRepairsByResidentId(Integer residentId) {
        return repairRepository.findByResidentIdOrderByCreatedAtDesc(residentId);
    }

    public List<Repair> getRepairsByStatus(String status) {
        return repairRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Repair addRepair(Repair repair) {
        repair.setCreatedAt(new Timestamp(new Date().getTime()));
        repair.setStatus("PENDING");
        return repairRepository.save(repair);
    }

    public Repair updateRepairStatus(Integer id, String status, String staffNotes) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Repair", "id", id));

        repair.setStatus(status);
        repair.setStaffNotes(staffNotes);

        if ("COMPLETED".equals(status)) {
            repair.setCompletedAt(new Timestamp(new Date().getTime()));
        }

        return repairRepository.save(repair);
    }
}