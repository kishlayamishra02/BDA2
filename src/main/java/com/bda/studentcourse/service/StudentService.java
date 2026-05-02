package com.bda.studentcourse.service;

import com.bda.studentcourse.entity.Student;
import com.bda.studentcourse.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Fetch all students with their course info (uses JOIN FETCH).
     */
    public List<Student> getAllStudentsWithCourse() {
        return studentRepository.findAllStudentsWithCourse();
    }

    /**
     * Find a student by ID.
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Save a new student. Validates email uniqueness.
     */
    public Student saveStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DataIntegrityViolationException(
                "Email '" + student.getEmail() + "' is already registered.");
        }
        return studentRepository.save(student);
    }

    /**
     * Update an existing student's details.
     */
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

        // Allow same email if it belongs to the same student record
        if (!existing.getEmail().equals(updatedStudent.getEmail())
                && studentRepository.existsByEmail(updatedStudent.getEmail())) {
            throw new DataIntegrityViolationException(
                "Email '" + updatedStudent.getEmail() + "' is already registered.");
        }

        existing.setFirstName(updatedStudent.getFirstName());
        existing.setLastName(updatedStudent.getLastName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setDateOfBirth(updatedStudent.getDateOfBirth());
        existing.setMajor(updatedStudent.getMajor());
        existing.setCourse(updatedStudent.getCourse());
        return studentRepository.save(existing);
    }

    /**
     * Get students enrolled in a specific course.
     */
    public List<Student> getStudentsByCourse(Long courseId) {
        return studentRepository.findStudentsByCourseId(courseId);
    }

    /**
     * Delete a student by ID.
     */
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
