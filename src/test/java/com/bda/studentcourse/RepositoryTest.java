package com.bda.studentcourse;

import com.bda.studentcourse.entity.Course;
import com.bda.studentcourse.entity.Student;
import com.bda.studentcourse.repository.CourseRepository;
import com.bda.studentcourse.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course savedCourse;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        savedCourse = courseRepository.save(
            new Course(null, "CS101", "Intro to CS", "Computer Science", 3, null));

        studentRepository.save(
            new Student(null, "Alice", "Johnson", "alice@test.com", "2001-03-15",
                        "Computer Science", savedCourse));
        studentRepository.save(
            new Student(null, "Bob", "Smith", "bob@test.com", "2002-07-22",
                        "Mathematics", savedCourse));
    }

    // ─────────── Course Repository Tests ───────────

    @Test
    @DisplayName("Should find course by code")
    void testFindCourseByCourseCode() {
        Optional<Course> found = courseRepository.findByCourseCode("CS101");
        assertThat(found).isPresent();
        assertThat(found.get().getCourseName()).isEqualTo("Intro to CS");
    }

    @Test
    @DisplayName("Should return true if course code exists")
    void testExistsByCourseCode() {
        assertThat(courseRepository.existsByCourseCode("CS101")).isTrue();
        assertThat(courseRepository.existsByCourseCode("INVALID")).isFalse();
    }

    @Test
    @DisplayName("Should save and retrieve course")
    void testSaveCourse() {
        Course course = courseRepository.save(
            new Course(null, "MATH201", "Calculus II", "Mathematics", 4, null));
        assertThat(course.getCourseId()).isNotNull();
    }

    @Test
    @DisplayName("Should update course name")
    void testUpdateCourse() {
        savedCourse.setCourseName("Advanced CS");
        Course updated = courseRepository.save(savedCourse);
        assertThat(updated.getCourseName()).isEqualTo("Advanced CS");
    }

    // ─────────── Student Repository Tests ───────────

    @Test
    @DisplayName("Custom JPQL JOIN: Should fetch all students with course")
    void testFindAllStudentsWithCourse() {
        List<Student> students = studentRepository.findAllStudentsWithCourse();
        assertThat(students).hasSize(2);
        assertThat(students.get(0).getCourse()).isNotNull();
        assertThat(students.get(0).getCourse().getCourseCode()).isEqualTo("CS101");
    }

    @Test
    @DisplayName("Should find students by course ID")
    void testFindStudentsByCourseId() {
        List<Student> students = studentRepository.findStudentsByCourseId(savedCourse.getCourseId());
        assertThat(students).hasSize(2);
    }

    @Test
    @DisplayName("Should return true if email exists")
    void testExistsByEmail() {
        assertThat(studentRepository.existsByEmail("alice@test.com")).isTrue();
        assertThat(studentRepository.existsByEmail("nonexistent@test.com")).isFalse();
    }

    @Test
    @DisplayName("Should find students by major")
    void testFindByMajor() {
        List<Student> cs = studentRepository.findByMajor("Computer Science");
        assertThat(cs).hasSize(1);
        assertThat(cs.get(0).getFirstName()).isEqualTo("Alice");
    }
}
