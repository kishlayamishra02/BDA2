package com.bda.studentcourse;

import com.bda.studentcourse.entity.Course;
import com.bda.studentcourse.entity.Student;
import com.bda.studentcourse.repository.CourseRepository;
import com.bda.studentcourse.repository.StudentRepository;
import com.bda.studentcourse.service.CourseService;
import com.bda.studentcourse.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    // ─── CourseService tests ───
    @Mock  private CourseRepository courseRepository;
    @InjectMocks private CourseService courseService;

    // ─── StudentService tests ───
    @Mock  private StudentRepository studentRepository;
    @InjectMocks private StudentService studentService;

    private Course mockCourse;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        mockCourse = new Course(1L, "CS101", "Intro to CS", "Computer Science", 3, null);
        mockStudent = new Student(1L, "Alice", "Johnson", "alice@uni.edu",
                                  "2001-03-15", "CS", mockCourse);
    }

    // ─────────── CourseService Tests ───────────

    @Test
    @DisplayName("Should return all courses")
    void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(mockCourse));
        List<Course> courses = courseService.getAllCourses();
        assertThat(courses).hasSize(1);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save course when code is unique")
    void testSaveCourseSuccess() {
        when(courseRepository.existsByCourseCode("CS101")).thenReturn(false);
        when(courseRepository.save(mockCourse)).thenReturn(mockCourse);

        Course saved = courseService.saveCourse(mockCourse);
        assertThat(saved.getCourseCode()).isEqualTo("CS101");
        verify(courseRepository).save(mockCourse);
    }

    @Test
    @DisplayName("Should throw exception when course code is duplicate")
    void testSaveCourseDuplicateCode() {
        when(courseRepository.existsByCourseCode("CS101")).thenReturn(true);
        assertThatThrownBy(() -> courseService.saveCourse(mockCourse))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("CS101");
    }

    @Test
    @DisplayName("Should find course by ID")
    void testGetCourseById() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        Optional<Course> found = courseService.getCourseById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getCourseName()).isEqualTo("Intro to CS");
    }

    @Test
    @DisplayName("Should update course fields")
    void testUpdateCourse() {
        Course updated = new Course(null, "CS101", "Updated CS", "CS Dept", 4, null);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(mockCourse);

        Course result = courseService.updateCourse(1L, updated);
        verify(courseRepository).save(mockCourse);
    }

    // ─────────── StudentService Tests ───────────

    @Test
    @DisplayName("Should return all students with courses (JOIN)")
    void testGetAllStudentsWithCourse() {
        when(studentRepository.findAllStudentsWithCourse()).thenReturn(Arrays.asList(mockStudent));
        List<Student> students = studentService.getAllStudentsWithCourse();
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getCourse()).isNotNull();
        verify(studentRepository).findAllStudentsWithCourse();
    }

    @Test
    @DisplayName("Should save student when email is unique")
    void testSaveStudentSuccess() {
        when(studentRepository.existsByEmail("alice@uni.edu")).thenReturn(false);
        when(studentRepository.save(mockStudent)).thenReturn(mockStudent);

        Student saved = studentService.saveStudent(mockStudent);
        assertThat(saved.getEmail()).isEqualTo("alice@uni.edu");
    }

    @Test
    @DisplayName("Should throw exception on duplicate email")
    void testSaveStudentDuplicateEmail() {
        when(studentRepository.existsByEmail("alice@uni.edu")).thenReturn(true);
        assertThatThrownBy(() -> studentService.saveStudent(mockStudent))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("alice@uni.edu");
    }

    @Test
    @DisplayName("Should throw exception if student not found on update")
    void testUpdateStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> studentService.updateStudent(99L, mockStudent))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Student not found");
    }
}
