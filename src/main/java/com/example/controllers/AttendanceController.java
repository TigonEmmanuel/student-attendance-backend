package com.example.controllers;

import com.example.models.Attendance;
import com.example.models.Student;
import com.example.repositories.AttendanceRepository;
import com.example.repositories.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceController(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    // ✅ 1. Mark Attendance
    @PostMapping
    public ResponseEntity<?> markAttendance(@RequestBody Attendance attendance) {
        Optional<Student> student = studentRepository.findById(attendance.getStudent().getId());
        if (student.isEmpty()) {
            return ResponseEntity.badRequest().body("Student not found");
        }
        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Attendance marked successfully");
    }

    // ✅ 2. Update Attendance (with transaction and forced flush)
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAttendance(@PathVariable Long id, @RequestBody Attendance updatedAttendance) {
        System.out.println("🚀 Update request received for attendance ID: " + id);

        Optional<Attendance> existingAttendance = attendanceRepository.findById(id);
        if (existingAttendance.isEmpty()) {
            System.out.println("❌ Attendance record not found: " + id);
            return ResponseEntity.badRequest().body("Attendance record not found");
        }

        Attendance attendance = existingAttendance.get();
        System.out.println("🔍 Before Update: " + attendance.getStatus());

        attendance.setStatus(updatedAttendance.getStatus());
        attendanceRepository.saveAndFlush(attendance); // Force Hibernate to commit immediately

        System.out.println("✅ After Update: " + attendance.getStatus());
        return ResponseEntity.ok("Attendance updated successfully");
    }

    // ✅ 3. View Attendance by Student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<Attendance> attendanceRecords = attendanceRepository.findByStudentId(studentId);
        return ResponseEntity.ok(attendanceRecords);
    }

    // ✅ 4. Delete Attendance Record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id) {
        if (!attendanceRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Attendance record not found");
        }
        attendanceRepository.deleteById(id);
        return ResponseEntity.ok("Attendance record deleted successfully");
    }
}