package com.example.repositories;

import com.example.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @SuppressWarnings("null")
    Optional<Student> findById(Long id); // ✅ Replacing findByUsername with findById since 'username' doesn't exist in Student model
}
