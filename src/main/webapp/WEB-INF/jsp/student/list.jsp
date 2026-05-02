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
  <a href="${pageContext.request.contextPath}/" class="navbar-brand">🎓 <span>EduTrack</span></a>
  <div class="nav-links">
    <a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
    <a href="${pageContext.request.contextPath}/students" class="nav-link active">Students</a>
    <a href="${pageContext.request.contextPath}/courses"  class="nav-link">Courses</a>
  </div>
</nav>

<div class="container">

  <div class="page-header">
    <h1 class="page-title">All Students</h1>
    <a href="${pageContext.request.contextPath}/students/new" class="btn btn-primary">
      ➕ Enroll Student
    </a>
  </div>

  <!-- Flash messages -->
  <c:if test="${not empty successMsg}">
    <div class="alert alert-success">✅ ${successMsg}</div>
  </c:if>
  <c:if test="${not empty errorMsg}">
    <div class="alert alert-danger">❌ ${errorMsg}</div>
  </c:if>

  <div class="card">
    <div class="table-wrapper">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Student Name</th>
            <th>Email</th>
            <th>Date of Birth</th>
            <th>Major</th>
            <th>Enrolled Course</th>
            <th>Dept.</th>
            <th>Credits</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="s" items="${students}" varStatus="st">
          <tr>
            <td><span class="badge badge-blue">${st.count}</span></td>
            <td><strong>${s.firstName} ${s.lastName}</strong></td>
            <td>${s.email}</td>
            <td>${s.dateOfBirth}</td>
            <td><span class="badge badge-green">${s.major}</span></td>
            <td><strong>${s.course.courseName}</strong><br/>
                <small style="color:var(--text-muted)">${s.course.courseCode}</small></td>
            <td>${s.course.department}</td>
            <td><span class="badge badge-cyan">${s.course.credits}</span></td>
            <td>
              <a href="${pageContext.request.contextPath}/students/edit/${s.studentId}"
                 class="btn btn-warning" style="padding:.35rem .8rem;font-size:.8rem;">✏️ Edit</a>
            </td>
          </tr>
          </c:forEach>
          <c:if test="${empty students}">
            <tr><td colspan="9" style="text-align:center;color:var(--text-muted);padding:2rem">
              No students found. <a href="${pageContext.request.contextPath}/students/new">Enroll one now!</a>
            </td></tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>

</div>
</body>
</html>
