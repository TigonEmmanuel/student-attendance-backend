package com.example.models;

import jakarta.persistence.*;
//import java.util.Date;
import java.time.LocalDate;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate dob;
    private String gender;
    private String faculty;
    private String department;
    private String programme;
    private String level;
    private String semester;

    // Constructors
    public Student() {}

    public Student(String name, LocalDate dob, String gender, String faculty, String department, String programme, String level, String semester) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.faculty = faculty;
        this.department = department;
        this.programme = programme;
        this.level = level;
        this.semester = semester;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getProgramme() { return programme; }
    public void setProgramme(String programme) { this.programme = programme; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}

// Enum for Gender
enum Gender {
    MALE, FEMALE, OTHER
}

