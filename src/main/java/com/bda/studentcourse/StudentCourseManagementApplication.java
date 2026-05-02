package com.bda.studentcourse;

import com.bda.studentcourse.entity.Course;
import com.bda.studentcourse.entity.Student;
import com.bda.studentcourse.repository.CourseRepository;
import com.bda.studentcourse.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class StudentCourseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentCourseManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(StudentRepository studentRepo, CourseRepository courseRepo) {
        return args -> {
            // Only seed if tables are empty
            if (studentRepo.count() == 0 && courseRepo.count() == 0) {

                // Create 10 Courses
                Course c1  = courseRepo.save(new Course(null, "CS101",  "Introduction to Computer Science", "Computer Science", 3, null));
                Course c2  = courseRepo.save(new Course(null, "MATH201","Calculus II",                       "Mathematics",       4, null));
                Course c3  = courseRepo.save(new Course(null, "PHY301", "Quantum Mechanics",                 "Physics",           3, null));
                Course c4  = courseRepo.save(new Course(null, "ENG401", "Technical Writing",                 "English",           2, null));
                Course c5  = courseRepo.save(new Course(null, "DS501",  "Data Structures & Algorithms",      "Computer Science", 4, null));
                Course c6  = courseRepo.save(new Course(null, "AI601",  "Machine Learning Fundamentals",     "AI/ML",             3, null));
                Course c7  = courseRepo.save(new Course(null, "DB701",  "Database Management Systems",       "Computer Science", 3, null));
                Course c8  = courseRepo.save(new Course(null, "NET801", "Computer Networks",                 "Networking",        3, null));
                Course c9  = courseRepo.save(new Course(null, "SEC901", "Cybersecurity Basics",              "Security",          3, null));
                Course c10 = courseRepo.save(new Course(null, "WEB101", "Web Development",                  "Computer Science", 3, null));

                // Create 10 Students (each enrolled in one course)
                studentRepo.saveAll(List.of(
                    new Student(null, "Alice",   "Johnson",  "alice@uni.edu",   "2001-03-15", "Computer Science", c1),
                    new Student(null, "Bob",     "Smith",    "bob@uni.edu",     "2002-07-22", "Mathematics",      c2),
                    new Student(null, "Carol",   "White",    "carol@uni.edu",   "2000-11-05", "Physics",          c3),
                    new Student(null, "David",   "Brown",    "david@uni.edu",   "2001-06-18", "English",          c4),
                    new Student(null, "Eva",     "Martinez", "eva@uni.edu",     "2003-01-30", "Computer Science", c5),
                    new Student(null, "Frank",   "Lee",      "frank@uni.edu",   "2002-09-12", "AI/ML",            c6),
                    new Student(null, "Grace",   "Kim",      "grace@uni.edu",   "2001-04-25", "Computer Science", c7),
                    new Student(null, "Henry",   "Davis",    "henry@uni.edu",   "2000-12-08", "Networking",       c8),
                    new Student(null, "Isabelle","Wilson",   "isabelle@uni.edu","2003-05-14", "Security",         c9),
                    new Student(null, "Jack",    "Taylor",   "jack@uni.edu",    "2002-02-27", "Computer Science", c10)
                ));

                System.out.println("✅ Database seeded with 10 courses and 10 students.");
            }
        };
    }
}
