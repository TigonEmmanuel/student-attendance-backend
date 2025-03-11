package com.example.controllers;

import com.example.models.Attendance;
import com.example.models.Student;
import com.example.repositories.AttendanceRepository;
import com.example.repositories.StudentRepository;
import com.example.services.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final String DEFAULT_CODE = "12345"; // Default login code
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final JwtUtil jwtUtil;

    public StudentController(StudentRepository studentRepository, AttendanceRepository attendanceRepository, JwtUtil jwtUtil) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        Optional<Student> student = studentRepository.findById(1L); // Dummy student fetch for now

        if (DEFAULT_CODE.equals(code) && student.isPresent()) {
            // ✅ Generate JWT token with real student ID
            UserDetails studentUser = User.withUsername(String.valueOf(student.get().getId()))
                                          .password("")
                                          .authorities("ROLE_STUDENT")
                                          .build();
            String token = jwtUtil.generateToken(studentUser);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body("Invalid login code!");
        }
    }

    // ✅ View student attendance
        @GetMapping("/attendance")
public ResponseEntity<?> getStudentAttendance(@AuthenticationPrincipal UserDetails userDetails) {
    String studentId = userDetails.getUsername();
    List<Attendance> attendanceList = attendanceRepository.findByStudentId(Long.parseLong(studentId));

    var response = attendanceList.stream().map(att -> {
        Map<String, Object> orderedResponse = new LinkedHashMap<>();
        orderedResponse.put("id", att.getId());
        orderedResponse.put("studentId", att.getStudent().getId());
        orderedResponse.put("studentName", att.getStudent().getName());
        orderedResponse.put("date", att.getDate());
        orderedResponse.put("status", att.getStatus()); // ✅ Now guaranteed to be last

        return orderedResponse;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(response);
}

    }
        