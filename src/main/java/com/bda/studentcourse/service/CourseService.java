package com.bda.studentcourse.service;

import com.bda.studentcourse.entity.Course;
import com.bda.studentcourse.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Fetch all courses from the database.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Find a course by its ID.
     */
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    /**
     * Save a new course. Throws exception if courseCode already exists.
     */
    public Course saveCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new DataIntegrityViolationException(
                "Course code '" + course.getCourseCode() + "' already exists.");
        }
        return courseRepository.save(course);
    }

    /**
     * Update an existing course by ID.
     */
    public Course updateCourse(Long id, Course updatedCourse) {
        Course existing = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));

        // Check uniqueness of code (allow same code if it's the same record)
        if (!existing.getCourseCode().equals(updatedCourse.getCourseCode())
                && courseRepository.existsByCourseCode(updatedCourse.getCourseCode())) {
            throw new DataIntegrityViolationException(
                "Course code '" + updatedCourse.getCourseCode() + "' is already in use.");
        }

        existing.setCourseCode(updatedCourse.getCourseCode());
        existing.setCourseName(updatedCourse.getCourseName());
        existing.setDepartment(updatedCourse.getDepartment());
        existing.setCredits(updatedCourse.getCredits());
        return courseRepository.save(existing);
    }

    /**
     * Delete a course by ID.
     */
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
