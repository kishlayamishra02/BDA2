package com.bda.studentcourse.controller;

import com.bda.studentcourse.entity.Course;
import com.bda.studentcourse.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // ─────────── READ: List all courses ───────────
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("pageTitle", "All Courses");
        return "course/list";
    }

    // ─────────── CREATE: Show form ───────────
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("pageTitle", "Add New Course");
        return "course/form";
    }

    // ─────────── CREATE: Handle form submission ───────────
    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Course");
            return "course/form";
        }
        try {
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMsg", "Course added successfully!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("pageTitle", "Add New Course");
            return "course/form";
        }
        return "redirect:/courses";
    }

    // ─────────── UPDATE: Show pre-filled edit form ───────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Course course = courseService.getCourseById(id)
            .orElse(null);
        if (course == null) {
            ra.addFlashAttribute("errorMsg", "Course not found!");
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        model.addAttribute("pageTitle", "Edit Course");
        return "course/form";
    }

    // ─────────── UPDATE: Handle update submission ───────────
    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("course") Course course,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Course");
            return "course/form";
        }
        try {
            courseService.updateCourse(id, course);
            redirectAttributes.addFlashAttribute("successMsg", "Course updated successfully!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("pageTitle", "Edit Course");
            return "course/form";
        }
        return "redirect:/courses";
    }
}
