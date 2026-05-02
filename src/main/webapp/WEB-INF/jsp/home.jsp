<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>${pageTitle} | EduTrack</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/WEB-INF/css/style.css"/>
</head>
<body>

<nav class="navbar">
  <a href="${pageContext.request.contextPath}/" class="navbar-brand">
    🎓 <span>EduTrack</span>
  </a>
  <div class="nav-links">
    <a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
    <a href="${pageContext.request.contextPath}/students" class="nav-link">Students</a>
    <a href="${pageContext.request.contextPath}/courses"  class="nav-link">Courses</a>
  </div>
</nav>

<div class="container">

  <div class="hero">
    <h1 class="hero-title">Student Course<br/>Management System</h1>
    <p class="hero-sub">Manage student enrollments and course offerings with ease.</p>

    <div class="action-grid">
      <a href="${pageContext.request.contextPath}/students" class="action-card">
        <span class="action-icon">👨‍🎓</span>
        <span class="action-title">View Students</span>
        <span class="action-desc">Browse all enrolled students and their courses</span>
      </a>
      <a href="${pageContext.request.contextPath}/students/new" class="action-card">
        <span class="action-icon">➕</span>
        <span class="action-title">Enroll Student</span>
        <span class="action-desc">Register a new student into a course</span>
      </a>
      <a href="${pageContext.request.contextPath}/courses" class="action-card">
        <span class="action-icon">📚</span>
        <span class="action-title">View Courses</span>
        <span class="action-desc">Browse all available courses</span>
      </a>
      <a href="${pageContext.request.contextPath}/courses/new" class="action-card">
        <span class="action-icon">🆕</span>
        <span class="action-title">Add Course</span>
        <span class="action-desc">Create a new course in the system</span>
      </a>
    </div>
  </div>

</div>

</body>
</html>
