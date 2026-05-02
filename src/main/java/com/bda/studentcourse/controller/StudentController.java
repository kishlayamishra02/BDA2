package com.bda.studentcourse.controller;

import com.bda.studentcourse.entity.Course;
import com.bda.studentcourse.entity.Student;
import com.bda.studentcourse.service.CourseService;
import com.bda.studentcourse.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    // ─────────── READ: List all students with course (uses INNER JOIN) ───────────
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudentsWithCourse();
        model.addAttribute("students", students);
        model.addAttribute("pageTitle", "All Students");
        return "student/list";
    }

    // ─────────── CREATE: Show form ───────────
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("pageTitle", "Enroll New Student");
        return "student/form";
    }

    // ─────────── CREATE: Handle submission ───────────
    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("pageTitle", "Enroll New Student");
            return "student/form";
        }
        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("successMsg", "Student enrolled successfully!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("pageTitle", "Enroll New Student");
            return "student/form";
        }
        return "redirect:/students";
    }

    // ─────────── UPDATE: Show pre-filled form ───────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Student student = studentService.getStudentById(id).orElse(null);
        if (student == null) {
            ra.addFlashAttribute("errorMsg", "Student not found!");
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("pageTitle", "Update Student");
        return "student/form";
    }

    // ─────────── UPDATE: Handle submission ───────────
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("pageTitle", "Update Student");
            return "student/form";
        }
        try {
            studentService.updateStudent(id, student);
            redirectAttributes.addFlashAttribute("successMsg", "Student updated successfully!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("pageTitle", "Update Student");
            return "student/form";
        }
        return "redirect:/students";
    }
}
