package com.bda.studentcourse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @NotBlank(message = "First name is required")
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Date of birth is required")
    @Column(nullable = false)
    private String dateOfBirth;

    @NotBlank(message = "Major is required")
    @Column(nullable = false, length = 50)
    private String major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Override
    public String toString() {
        return "Student{studentId=" + studentId + ", firstName='" + firstName +
               "', lastName='" + lastName + "', email='" + email +
               "', major='" + major + "'}";
    }
}
