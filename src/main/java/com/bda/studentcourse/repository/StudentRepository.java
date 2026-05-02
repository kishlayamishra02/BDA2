package com.bda.studentcourse.repository;

import com.bda.studentcourse.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Custom Query: Inner JOIN between Student and Course.
     * Returns all students along with their enrolled course information.
     * Uses JPQL JOIN FETCH to avoid N+1 query problem.
     */
    @Query("SELECT s FROM Student s JOIN FETCH s.course c")
    List<Student> findAllStudentsWithCourse();

    /**
     * Custom method: Find students by their major field.
     */
    List<Student> findByMajor(String major);

    /**
     * Custom method: Check if an email already exists (for integrity check).
     */
    boolean existsByEmail(String email);

    /**
     * Custom method: Find students enrolled in a specific course by courseId.
     */
    @Query("SELECT s FROM Student s JOIN FETCH s.course c WHERE c.courseId = :courseId")
    List<Student> findStudentsByCourseId(Long courseId);
}
