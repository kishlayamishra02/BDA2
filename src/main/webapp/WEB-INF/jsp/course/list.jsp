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
    <a href="${pageContext.request.contextPath}/students" class="nav-link">Students</a>
    <a href="${pageContext.request.contextPath}/courses"  class="nav-link active">Courses</a>
  </div>
</nav>

<div class="container">

  <div class="page-header">
    <h1 class="page-title">All Courses</h1>
    <a href="${pageContext.request.contextPath}/courses/new" class="btn btn-primary">
      ➕ Add Course
    </a>
  </div>

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
            <th>Code</th>
            <th>Course Name</th>
            <th>Department</th>
            <th>Credits</th>
            <th>Students</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="c" items="${courses}" varStatus="st">
          <tr>
            <td><span class="badge badge-blue">${st.count}</span></td>
            <td><code style="color:var(--primary-light)">${c.courseCode}</code></td>
            <td><strong>${c.courseName}</strong></td>
            <td><span class="badge badge-cyan">${c.department}</span></td>
            <td><span class="badge badge-green">${c.credits}</span></td>
            <td>${c.students != null ? c.students.size() : 0}</td>
            <td>
              <a href="${pageContext.request.contextPath}/courses/edit/${c.courseId}"
                 class="btn btn-warning" style="padding:.35rem .8rem;font-size:.8rem;">✏️ Edit</a>
            </td>
          </tr>
          </c:forEach>
          <c:if test="${empty courses}">
            <tr><td colspan="7" style="text-align:center;color:var(--text-muted);padding:2rem">
              No courses found. <a href="${pageContext.request.contextPath}/courses/new">Add one now!</a>
            </td></tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>

</div>
</body>
</html>
