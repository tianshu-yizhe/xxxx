package com.property.service;

import com.property.exception.ResourceNotFoundException;
import com.property.model.Fee;
import com.property.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeService {

    @Autowired
    private FeeRepository feeRepository;

    public List<Fee> getFeesByResidentId(Integer residentId) {
        return feeRepository.findByResidentIdOrderByDueDateDesc(residentId);
    }

    public List<Fee> getUnpaidFees() {
        return feeRepository.findByStatusOrderByDueDateAsc("UNPAID");
    }

    public Fee addFee(Fee fee) {
        return feeRepository.save(fee);
    }

    public Fee updateFeeStatus(Integer id, String status) {
        Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fee", "id", id));

        fee.setStatus(status);
        return feeRepository.save(fee);
    }
}