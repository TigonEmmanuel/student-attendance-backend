package com.example.controllers;

import com.example.models.Admin;
//import com.example.models.Attendance;
import com.example.models.Student;
import com.example.repositories.AdminRepository;
import com.example.repositories.StudentRepository;
import com.example.repositories.AttendanceRepository; // ✅ Import AttendanceRepository
import com.example.services.JwtUtil;
//import com.lowagie.text.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;
//import java.util.stream.Collectors; // ✅ Import Collectors

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository; // ✅ Inject AttendanceRepository
    private final PasswordEncoder passwordEncoder;

    public AdminController(JwtUtil jwtUtil, AdminRepository adminRepository, StudentRepository studentRepository, 
                           AttendanceRepository attendanceRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository; // ✅ Initialize attendanceRepository
        this.passwordEncoder = passwordEncoder;
    }

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (adminRepository.findByUsername(username) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 8 characters long, contain uppercase, lowercase, and a number"));
        }

        String hashedPassword = passwordEncoder.encode(password);
        Admin newAdmin = new Admin(username, hashedPassword);
        adminRepository.save(newAdmin);

        return ResponseEntity.ok(Map.of("message", "Admin registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Admin admin = adminRepository.findByUsername(username);
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        UserDetails adminUser = User.withUsername(username)
                                    .password("")
                                    .authorities("ROLE_ADMIN")
                                    .build();
        String token = jwtUtil.generateToken(adminUser);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/students")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        logger.info("Adding a new student: {}", student.getName());
        studentRepository.save(student);
        return ResponseEntity.ok(Map.of("message", "Student added successfully"));
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        var students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

        @GetMapping("/attendance") // ✅ Ensure this annotation is present
    public ResponseEntity<?> getAllAttendance() {
        var  attendance = attendanceRepository.findAll(); // ✅ Use repository
        return ResponseEntity.ok(attendance);
    }


    
}