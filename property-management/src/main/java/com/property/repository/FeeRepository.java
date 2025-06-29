package com.property.repository;

import com.property.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Integer> {
    List<Fee> findByResidentIdOrderByDueDateDesc(Integer residentId);
    List<Fee> findByStatusOrderByDueDateAsc(String status);
}