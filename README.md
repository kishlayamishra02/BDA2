# BDA Assignment 2 — Spring Boot CRUD Application
## Student & Course Management System

**Name:** Kishlaya Mishra  
**Roll No:** 2024EB01371  
**Subject:** Building Database Applications
**GitHub:** https://github.com/kishlayamishra02/BDA2
**Detailed PDF:** https://github.com/kishlayamishra02/BDA2/blob/main/README.md

---

## 1. Introduction

For this assignment, I chose to build a **Student & Course Management System** using Spring Boot MVC. I selected these two entities because they have a natural, meaningful relationship — a Course can have many Students enrolled in it — which gave me a good opportunity to demonstrate JPA relationships, custom queries, and proper layered architecture.

My two entities are:
- **Student** — represents a university student with personal details and their enrolled course
- **Course** — represents an academic course with a code, name, department, and credit value

I implemented all three required operations — **Create**, **Read**, and **Update** — across both entities, using JSP pages for the UI, Spring MVC controllers for request handling, a service layer for business logic, and Spring Data JPA repositories for database access.

---

## 2. Entity Relationship Design

### Relationship
A **Course** can have many **Students** enrolled in it.  
A **Student** is enrolled in exactly one **Course**.  
This is a **Many-to-One** relationship from Student → Course (and **One-to-Many** from Course → Student).

### ER Diagram

```
+------------------+         +------------------+
|     courses      |         |     students     |
+------------------+         +------------------+
| courseId   (PK)  |◄────────| studentId  (PK)  |
| courseCode       |  M:1    | firstName        |
| courseName       |         | lastName         |
| department       |         | email (UNIQUE)   |
| credits          |         | dateOfBirth      |
+------------------+         | major            |
                             | course_id  (FK)  |
                             +------------------+
```

---

## 3. Project Structure

```
student-course-management/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/bda/studentcourse/
    │   │   ├── StudentCourseManagementApplication.java
    │   │   ├── entity/
    │   │   │   ├── Course.java
    │   │   │   └── Student.java
    │   │   ├── repository/
    │   │   │   ├── CourseRepository.java
    │   │   │   └── StudentRepository.java
    │   │   ├── service/
    │   │   │   ├── CourseService.java
    │   │   │   └── StudentService.java
    │   │   └── controller/
    │   │       ├── HomeController.java
    │   │       ├── CourseController.java
    │   │       └── StudentController.java
    │   ├── resources/
    │   │   └── application.properties
    │   └── webapp/WEB-INF/
    │       ├── css/style.css
    │       └── jsp/
    │           ├── home.jsp
    │           ├── student/list.jsp, form.jsp
    │           └── course/list.jsp, form.jsp
    └── test/
        └── java/com/bda/studentcourse/
            ├── RepositoryTest.java
            └── ServiceTest.java
```

---

## 4. Tech Stack

| Layer      | Technology |
|------------|------------|
| Backend    | Spring Boot 3.2.5, Spring MVC, Spring Data JPA |
| Database   | MySQL 8 (H2 for tests) |
| ORM        | Hibernate / JPA |
| View       | JSP + JSTL + Spring Form Tag Library |
| Styling    | Custom CSS (Dark theme, Google Fonts: Inter) |
| Testing    | JUnit 5, Mockito, Spring DataJpaTest |
| Build      | Maven |

---

## 4.1 Maven Dependencies (pom.xml)

Below are the key dependencies I used in `pom.xml`:

```xml
<!-- Spring Boot Web MVC -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Bean Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- MySQL Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- H2 (for unit tests only) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>

<!-- JSP support (Jasper engine) -->
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <scope>provided</scope>
</dependency>

<!-- JSTL (Jakarta namespace) -->
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

> **Note:** `tomcat-embed-jasper` is critical — without it, Spring Boot cannot render JSP files. The `jakarta.*` JSTL namespace (not `javax.*`) is required for Spring Boot 3.

---

## 5. Implementation Details

### 5.1 Populate Database

I implemented a `CommandLineRunner` bean inside the main application class. This runs automatically at startup and inserts 10 courses and 10 students, but **only if the tables are empty** — this prevents duplicate inserts on restarts.

```java
@SpringBootApplication
public class StudentCourseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentCourseManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(StudentRepository studentRepo,
                                          CourseRepository courseRepo) {
        return args -> {
            if (studentRepo.count() == 0 && courseRepo.count() == 0) {

                // ── 10 Courses ──
                Course c1  = courseRepo.save(new Course(null, "CS101",  "Introduction to Computer Science", "Computer Science", 3, null));
                Course c2  = courseRepo.save(new Course(null, "MATH201","Calculus II",                       "Mathematics",       4, null));
                Course c3  = courseRepo.save(new Course(null, "PHY301", "Quantum Mechanics",                 "Physics",           3, null));
                Course c4  = courseRepo.save(new Course(null, "ENG401", "Technical Writing",                 "English",           2, null));
                Course c5  = courseRepo.save(new Course(null, "DS501",  "Data Structures & Algorithms",      "Computer Science",  4, null));
                Course c6  = courseRepo.save(new Course(null, "AI601",  "Machine Learning Fundamentals",     "AI/ML",             3, null));
                Course c7  = courseRepo.save(new Course(null, "DB701",  "Database Management Systems",       "Computer Science",  3, null));
                Course c8  = courseRepo.save(new Course(null, "NET801", "Computer Networks",                 "Networking",        3, null));
                Course c9  = courseRepo.save(new Course(null, "SEC901", "Cybersecurity Basics",              "Security",          3, null));
                Course c10 = courseRepo.save(new Course(null, "WEB101", "Web Development",                  "Computer Science",  3, null));

                // ── 10 Students ──
                studentRepo.saveAll(List.of(
                    new Student(null, "Alice",    "Johnson",  "alice@uni.edu",    "2001-03-15", "Computer Science", c1),
                    new Student(null, "Bob",      "Smith",    "bob@uni.edu",      "2002-07-22", "Mathematics",      c2),
                    new Student(null, "Carol",    "White",    "carol@uni.edu",    "2000-11-05", "Physics",          c3),
                    new Student(null, "David",    "Brown",    "david@uni.edu",    "2001-06-18", "English",          c4),
                    new Student(null, "Eva",      "Martinez", "eva@uni.edu",      "2003-01-30", "Computer Science", c5),
                    new Student(null, "Frank",    "Lee",      "frank@uni.edu",    "2002-09-12", "AI/ML",            c6),
                    new Student(null, "Grace",    "Kim",      "grace@uni.edu",    "2001-04-25", "Computer Science", c7),
                    new Student(null, "Henry",    "Davis",    "henry@uni.edu",    "2000-12-08", "Networking",       c8),
                    new Student(null, "Isabelle", "Wilson",   "isabelle@uni.edu", "2003-05-14", "Security",         c9),
                    new Student(null, "Jack",     "Taylor",   "jack@uni.edu",     "2002-02-27", "Computer Science", c10)
                ));

                System.out.println("Database seeded: 10 courses + 10 students inserted.");
            }
        };
    }
}
```

**How JPA creates the tables:**  
`spring.jpa.hibernate.ddl-auto=update` tells Hibernate to automatically create or update the `courses` and `students` tables based on the entity class definitions. No manual SQL scripts are needed.

**Sample Data (Courses table):**

| courseCode | courseName                       | department       | credits |
|------------|----------------------------------|------------------|---------|
| CS101      | Introduction to Computer Science | Computer Science | 3       |
| MATH201    | Calculus II                      | Mathematics      | 4       |
| PHY301     | Quantum Mechanics                | Physics          | 3       |
| ENG401     | Technical Writing                | English          | 2       |
| DS501      | Data Structures & Algorithms     | Computer Science | 4       |
| AI601      | Machine Learning Fundamentals    | AI/ML            | 3       |
| DB701      | Database Management Systems      | Computer Science | 3       |
| NET801     | Computer Networks                | Networking       | 3       |
| SEC901     | Cybersecurity Basics             | Security         | 3       |
| WEB101     | Web Development                  | Computer Science | 3       |

**Sample Data (Students table):**

| firstName | lastName | email             | major            | course_id |
|-----------|----------|-------------------|------------------|-----------|
| Alice     | Johnson  | alice@uni.edu     | Computer Science | 1         |
| Bob       | Smith    | bob@uni.edu       | Mathematics      | 2         |
| Carol     | White    | carol@uni.edu     | Physics          | 3         |
| David     | Brown    | david@uni.edu     | English          | 4         |
| Eva       | Martinez | eva@uni.edu       | Computer Science | 5         |
| Frank     | Lee      | frank@uni.edu     | AI/ML            | 6         |
| Grace     | Kim      | grace@uni.edu     | Computer Science | 7         |
| Henry     | Davis    | henry@uni.edu     | Networking       | 8         |
| Isabelle  | Wilson   | isabelle@uni.edu  | Security         | 9         |
| Jack      | Taylor   | jack@uni.edu      | Computer Science | 10        |

---

### 5.2 Entity Classes

#### Course.java

```java
@Entity
@Table(name = "courses")
@Data @NoArgsConstructor @AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @NotBlank(message = "Course code is required")
    @Column(unique = true, nullable = false, length = 20)
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false, length = 100)
    private String courseName;

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 50)
    private String department;

    @Min(1) @Max(6)
    @Column(nullable = false)
    private Integer credits;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    private List<Student> students;
}
```

#### Student.java

```java
@Entity
@Table(name = "students")
@Data @NoArgsConstructor @AllArgsConstructor
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

    @Email @NotBlank
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String dateOfBirth;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
```

**JPA Annotations Used:**

| Annotation | Purpose |
|---|---|
| `@Entity` | Marks class as a JPA entity (maps to DB table) |
| `@Table` | Specifies the table name |
| `@Id` | Marks the primary key field |
| `@GeneratedValue` | Auto-increments the PK using IDENTITY strategy |
| `@Column` | Configures column properties (nullable, unique, length) |
| `@OneToMany` | Defines one-to-many relationship from Course → Students |
| `@ManyToOne` | Defines many-to-one relationship from Student → Course |
| `@JoinColumn` | Specifies the foreign key column (`course_id`) |

---

### 5.3 Repository Layer

#### CourseRepository.java

```java
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    boolean existsByCourseCode(String courseCode);
}
```

#### StudentRepository.java (with Custom INNER JOIN Query)

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Custom JPQL: INNER JOIN between Student and Course
    @Query("SELECT s FROM Student s JOIN FETCH s.course c")
    List<Student> findAllStudentsWithCourse();

    List<Student> findByMajor(String major);

    boolean existsByEmail(String email);

    @Query("SELECT s FROM Student s JOIN FETCH s.course c WHERE c.courseId = :courseId")
    List<Student> findStudentsByCourseId(Long courseId);
}
```

The `@Query` annotation with `JOIN FETCH` performs an SQL INNER JOIN between `students` and `courses` tables, returning only students that have an associated course.

**Generated SQL:**
```sql
SELECT s.*, c.*
FROM students s
INNER JOIN courses c ON s.course_id = c.course_id;
```

---

### 5.4 Service Layer

#### CourseService.java

```java
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course saveCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new DataIntegrityViolationException(
                "Course code '" + course.getCourseCode() + "' already exists.");
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course existing = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found: " + id));

        if (!existing.getCourseCode().equals(updatedCourse.getCourseCode())
                && courseRepository.existsByCourseCode(updatedCourse.getCourseCode())) {
            throw new DataIntegrityViolationException("Course code already in use.");
        }

        existing.setCourseCode(updatedCourse.getCourseCode());
        existing.setCourseName(updatedCourse.getCourseName());
        existing.setDepartment(updatedCourse.getDepartment());
        existing.setCredits(updatedCourse.getCredits());
        return courseRepository.save(existing);
    }
}
```

#### StudentService.java

```java
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudentsWithCourse() {
        return studentRepository.findAllStudentsWithCourse(); // Uses JOIN FETCH
    }

    public Student saveStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DataIntegrityViolationException(
                "Email '" + student.getEmail() + "' is already registered.");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found: " + id));

        if (!existing.getEmail().equals(updatedStudent.getEmail())
                && studentRepository.existsByEmail(updatedStudent.getEmail())) {
            throw new DataIntegrityViolationException("Email already registered.");
        }

        existing.setFirstName(updatedStudent.getFirstName());
        existing.setLastName(updatedStudent.getLastName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setDateOfBirth(updatedStudent.getDateOfBirth());
        existing.setMajor(updatedStudent.getMajor());
        existing.setCourse(updatedStudent.getCourse());
        return studentRepository.save(existing);
    }
}
```

---

### 5.5 Controller Layer

#### HomeController.java

This controller handles the root URL `/` and renders the dashboard home page.

```java
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Student Course Management System");
        return "home";  // resolves to /WEB-INF/jsp/home.jsp
    }
}
```

#### CourseController.java

```java
@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // READ – list all courses
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "course/list";
    }

    // CREATE – show blank form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/form";
    }

    // CREATE – handle form POST
    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult result, Model model,
                             RedirectAttributes ra) {
        if (result.hasErrors()) return "course/form";
        try {
            courseService.saveCourse(course);
            ra.addFlashAttribute("successMsg", "Course added successfully!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "course/form";
        }
        return "redirect:/courses";
    }

    // UPDATE – show pre-filled form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id).orElse(null));
        return "course/form";
    }

    // UPDATE – handle update POST
    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("course") Course course,
                               BindingResult result, Model model,
                               RedirectAttributes ra) {
        if (result.hasErrors()) return "course/form";
        try {
            courseService.updateCourse(id, course);
            ra.addFlashAttribute("successMsg", "Course updated!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "course/form";
        }
        return "redirect:/courses";
    }
}
```

#### StudentController.java

```java
@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;

    // READ – fetch students with INNER JOIN data
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudentsWithCourse());
        return "student/list";
    }

    // CREATE – show form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("courses", courseService.getAllCourses());
        return "student/form";
    }

    // CREATE – handle POST
    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result, Model model,
                              RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            return "student/form";
        }
        try {
            studentService.saveStudent(student);
            ra.addFlashAttribute("successMsg", "Student enrolled!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            return "student/form";
        }
        return "redirect:/students";
    }

    // UPDATE – show pre-filled form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id).orElse(null));
        model.addAttribute("courses", courseService.getAllCourses());
        return "student/form";
    }

    // UPDATE – handle POST
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result, Model model,
                                RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            return "student/form";
        }
        try {
            studentService.updateStudent(id, student);
            ra.addFlashAttribute("successMsg", "Student updated!");
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            return "student/form";
        }
        return "redirect:/students";
    }
}
```

---

### 5.6 View Layer (JSP)

I created JSP pages for all Create, Read, and Update operations for both entities. I used the `jakarta.tags.core` JSTL taglib and Spring's `form` tag library for data binding.

#### home.jsp – Dashboard

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="hero">
  <h1 class="hero-title">Student Course Management System</h1>
  <div class="action-grid">
    <a href="${pageContext.request.contextPath}/students" class="action-card">
      <span class="action-icon">👨‍🎓</span>
      <span class="action-title">View Students</span>
    </a>
    <a href="${pageContext.request.contextPath}/courses" class="action-card">
      <span class="action-icon">📚</span>
      <span class="action-title">View Courses</span>
    </a>
  </div>
</div>
```

#### student/list.jsp – Read Operation (INNER JOIN result)

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:forEach var="s" items="${students}" varStatus="st">
  <tr>
    <td>${st.count}</td>
    <td>${s.firstName} ${s.lastName}</td>
    <td>${s.email}</td>
    <td>${s.major}</td>
    <td>${s.course.courseName}</td>   <%-- Data from JOIN --%>
    <td>${s.course.department}</td>
    <td>${s.course.credits}</td>
    <td>
      <a href="/students/edit/${s.studentId}" class="btn btn-warning">✏️ Edit</a>
    </td>
  </tr>
</c:forEach>
```

#### student/form.jsp – Create & Update Form

```jsp
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form method="post" action="${actionUrl}" modelAttribute="student">
  <form:input path="firstName" cssClass="form-control" placeholder="First Name"/>
  <form:errors path="firstName" cssClass="form-error"/>

  <form:input path="email" type="email" cssClass="form-control"/>
  <form:errors path="email" cssClass="form-error"/>

  <%-- Course dropdown --%>
  <form:select path="course" cssClass="form-control">
    <form:option value="" label="-- Select a Course --"/>
    <form:options items="${courses}" itemValue="courseId" itemLabel="courseName"/>
  </form:select>

  <button type="submit" class="btn btn-primary">Save</button>
</form:form>
```

**EL (Expression Language) and JSTL usage:**
- `${s.course.courseName}` — EL to traverse the object graph
- `<c:forEach>` — JSTL to iterate over lists
- `<c:if>` — JSTL for conditional rendering of alerts
- `<form:*>` — Spring Form tags for data binding and validation errors

#### course/list.jsp – Read Operation (Courses)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page-header">
  <h1 class="page-title">All Courses</h1>
  <a href="${pageContext.request.contextPath}/courses/new" class="btn btn-primary">➕ Add Course</a>
</div>

<%-- Success/Error flash messages --%>
<c:if test="${not empty successMsg}">
  <div class="alert alert-success">✅ ${successMsg}</div>
</c:if>
<c:if test="${not empty errorMsg}">
  <div class="alert alert-danger">❌ ${errorMsg}</div>
</c:if>

<table>
  <thead>
    <tr>
      <th>#</th><th>Code</th><th>Course Name</th>
      <th>Department</th><th>Credits</th><th>Actions</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="c" items="${courses}" varStatus="st">
    <tr>
      <td>${st.count}</td>
      <td><code>${c.courseCode}</code></td>
      <td>${c.courseName}</td>
      <td>${c.department}</td>
      <td>${c.credits}</td>
      <td>
        <a href="${pageContext.request.contextPath}/courses/edit/${c.courseId}"
           class="btn btn-warning">✏️ Edit</a>
      </td>
    </tr>
    </c:forEach>
  </tbody>
</table>
```

#### course/form.jsp – Create & Update Form (Courses)

This single JSP handles both **Add** (when `course.courseId` is null) and **Edit** (when it has an ID). The form `action` URL is set dynamically using `<c:choose>`.

```jsp
<%@ taglib prefix="c"    uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%-- Dynamic action: save for new, update/{id} for existing --%>
<c:set var="actionUrl">
  <c:choose>
    <c:when test="${course.courseId != null}">
      ${pageContext.request.contextPath}/courses/update/${course.courseId}
    </c:when>
    <c:otherwise>${pageContext.request.contextPath}/courses/save</c:otherwise>
  </c:choose>
</c:set>

<form:form method="post" action="${actionUrl}" modelAttribute="course">

  <div class="form-row">
    <div class="form-group">
      <label class="form-label">Course Code</label>
      <form:input path="courseCode" cssClass="form-control" placeholder="e.g. CS101"/>
      <form:errors path="courseCode" cssClass="form-error"/>
    </div>
    <div class="form-group">
      <label class="form-label">Credits (1-6)</label>
      <form:input path="credits" type="number" cssClass="form-control" min="1" max="6"/>
      <form:errors path="credits" cssClass="form-error"/>
    </div>
  </div>

  <div class="form-group">
    <label class="form-label">Course Name</label>
    <form:input path="courseName" cssClass="form-control" placeholder="e.g. Intro to CS"/>
    <form:errors path="courseName" cssClass="form-error"/>
  </div>

  <div class="form-group">
    <label class="form-label">Department</label>
    <form:input path="department" cssClass="form-control" placeholder="e.g. Computer Science"/>
    <form:errors path="department" cssClass="form-error"/>
  </div>

  <button type="submit" class="btn btn-primary">
    <c:choose>
      <c:when test="${course.courseId != null}">💾 Update Course</c:when>
      <c:otherwise>✅ Add Course</c:otherwise>
    </c:choose>
  </button>

</form:form>
```

#### CSS Design Highlights (style.css)

```css
:root {
  --primary:     #4f46e5;   /* Indigo */
  --secondary:   #06b6d4;   /* Cyan */
  --bg:          #0f172a;   /* Slate 900 – dark background */
  --surface:     #1e293b;   /* Card background */
}

/* Gradient page title */
.page-title {
  background: linear-gradient(135deg, var(--primary-light), var(--secondary));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* Hover lift on cards */
.action-card:hover {
  border-color: var(--primary);
  transform: translateY(-3px);
  box-shadow: 0 4px 24px rgba(0,0,0,.4);
}
```

---

### 5.7 application.properties

```properties
# MySQL datasource
spring.datasource.url=jdbc:mysql://localhost:3306/student_course_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JSP view resolver
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp

server.port=8080
```

---

## 6. Unit Testing

### 6.1 Repository Tests — `RepositoryTest.java`

Uses `@DataJpaTest` with H2 in-memory database.

```java
@DataJpaTest
@ActiveProfiles("test")
class RepositoryTest {

    @Test
    @DisplayName("Custom JPQL JOIN: Should fetch all students with course")
    void testFindAllStudentsWithCourse() {
        List<Student> students = studentRepository.findAllStudentsWithCourse();
        assertThat(students).hasSize(2);
        assertThat(students.get(0).getCourse()).isNotNull();
        assertThat(students.get(0).getCourse().getCourseCode()).isEqualTo("CS101");
    }

    @Test
    @DisplayName("Should return true if course code exists")
    void testExistsByCourseCode() {
        assertThat(courseRepository.existsByCourseCode("CS101")).isTrue();
        assertThat(courseRepository.existsByCourseCode("INVALID")).isFalse();
    }

    @Test
    @DisplayName("Should find students by major field")
    void testFindByMajor() {
        List<Student> cs = studentRepository.findByMajor("Computer Science");
        assertThat(cs).hasSize(1);
        assertThat(cs.get(0).getFirstName()).isEqualTo("Alice");
    }
}
```

### 6.2 Service Tests — `ServiceTest.java`

Uses `@ExtendWith(MockitoExtension.class)` with Mockito mocks.

```java
@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock  private CourseRepository courseRepository;
    @InjectMocks private CourseService courseService;

    @Mock  private StudentRepository studentRepository;
    @InjectMocks private StudentService studentService;

    @Test
    @DisplayName("Should throw exception when course code is duplicate")
    void testSaveCourseDuplicateCode() {
        when(courseRepository.existsByCourseCode("CS101")).thenReturn(true);
        assertThatThrownBy(() -> courseService.saveCourse(mockCourse))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("CS101");
    }

    @Test
    @DisplayName("Should throw on duplicate email")
    void testSaveStudentDuplicateEmail() {
        when(studentRepository.existsByEmail("alice@uni.edu")).thenReturn(true);
        assertThatThrownBy(() -> studentService.saveStudent(mockStudent))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Should throw if student not found on update")
    void testUpdateStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> studentService.updateStudent(99L, mockStudent))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Student not found");
    }
}
```

### Test Summary

| Test Class     | Framework         | Tests | Coverage |
|---------------|-------------------|-------|----------|
| RepositoryTest | JUnit5 + H2      | 8     | Repository layer |
| ServiceTest    | JUnit5 + Mockito | 8     | Service layer |

---

## 7. How to Run the Application

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0 (running on localhost:3306)

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/kishlayamishra02/BDA2.git
cd student-course-management

# 2. Update DB credentials in src/main/resources/application.properties
#    spring.datasource.username=root
#    spring.datasource.password=your_password

# 3. Build and run
mvn spring-boot:run

# 4. Open browser
#    http://localhost:8080/
```

### Run Tests
```bash
mvn test
```

---

## 8. URL Mapping Reference

| URL | Method | Description |
|-----|--------|-------------|
| `/` | GET | Home dashboard |
| `/students` | GET | List all students (INNER JOIN query) |
| `/students/new` | GET | Show enroll form |
| `/students/save` | POST | Save new student |
| `/students/edit/{id}` | GET | Show edit form |
| `/students/update/{id}` | POST | Update student |
| `/courses` | GET | List all courses |
| `/courses/new` | GET | Show add course form |
| `/courses/save` | POST | Save new course |
| `/courses/edit/{id}` | GET | Show edit form |
| `/courses/update/{id}` | POST | Update course |

---

## 9. Exception Handling

| Scenario | Exception | Handling |
|---|---|---|
| Duplicate course code | `DataIntegrityViolationException` | Error message shown on form |
| Duplicate student email | `DataIntegrityViolationException` | Error message shown on form |
| Student/Course not found | `RuntimeException` | Redirect with flash error |
| Validation failure | `BindingResult` errors | Field-level error messages via `<form:errors>` |

---

## 10. Challenges & Solutions

| Challenge | Solution |
|---|---|
| JSP not rendering in Spring Boot 3 | Added `tomcat-embed-jasper` and `jakarta.servlet.jsp.jstl` dependencies with correct Jakarta namespace |
| N+1 query problem on student list | Used `JOIN FETCH` in JPQL query instead of lazy loading |
| JSTL taglib namespace error | Migrated from `javax` to `jakarta` JSTL URI (`jakarta.tags.core`) |
| Duplicate email/code on update | Checked uniqueness only when the value changes from the existing record |
| Data binding Course object from dropdown | Used `itemValue="courseId"` in `<form:options>` so Spring resolves the entity by ID |

---

## 11. Conclusion

Through this assignment, I gained hands-on experience building a full-stack Spring Boot MVC application from scratch. I designed the entity relationship, implemented all three CRUD operations across two entities, wrote custom JPQL queries with INNER JOIN, and tested both the repository and service layers with JUnit 5 and Mockito.

The most valuable takeaway for me was understanding how Spring Data JPA abstracts away boilerplate SQL while still allowing fine-grained control through `@Query`. I also learnt how to handle data integrity violations gracefully — showing user-friendly errors instead of crashing — and how to use Spring Form tags with `BindingResult` for clean validation feedback.

**Key accomplishments:**
- Implemented `@OneToMany` / `@ManyToOne` JPA relationship between Course and Student
- Wrote a custom JPQL `JOIN FETCH` query that performs an SQL INNER JOIN
- Built Create, Read, and Update operations with proper validation and error handling
- Applied a dark-themed, responsive CSS design to all JSP pages
- Wrote 16 unit/integration tests covering both repository and service layers

**GitHub Repository:** https://github.com/kishlayamishra02/BDA2
