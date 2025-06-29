package com.property.repository;

import com.property.model.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Integer> {
    List<Repair> findByResidentIdOrderByCreatedAtDesc(Integer residentId);
    List<Repair> findByStatusOrderByCreatedAtDesc(String status);
}