package com.example.repositories;

import com.example.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // ✅ Add this custom query method
    List<Attendance> findByStudentId(Long studentId);
}
