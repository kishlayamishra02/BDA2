package com.bda.studentcourse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @NotBlank(message = "Course code is required")
    @Column(unique = true, nullable = false, length = 20)
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false, length = 100)
    private String courseName;

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 50)
    private String department;

    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits cannot exceed 6")
    @Column(nullable = false)
    private Integer credits;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students;

    @Override
    public String toString() {
        return "Course{courseId=" + courseId + ", courseCode='" + courseCode +
               "', courseName='" + courseName + "', department='" + department +
               "', credits=" + credits + "}";
    }
}
