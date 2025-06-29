package com.property.repository;

import com.property.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    List<Resident> findAllByOrderByRoomNumberAsc();

    @Query("SELECT r FROM Resident r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Resident> searchByNameOrRoomNumberOrPhone(@Param("keyword") String keyword);
}