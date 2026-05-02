package com.bda.studentcourse.repository;

import com.bda.studentcourse.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Custom method: Find a course by its unique course code.
     */
    Optional<Course> findByCourseCode(String courseCode);

    /**
     * Custom method: Check if a course code already exists.
     */
    boolean existsByCourseCode(String courseCode);
}
