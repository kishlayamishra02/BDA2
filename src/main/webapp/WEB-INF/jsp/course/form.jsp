<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"    uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <h1 class="page-title">${pageTitle}</h1>
    <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary">← Back</a>
  </div>

  <c:if test="${not empty errorMsg}">
    <div class="alert alert-danger">❌ ${errorMsg}</div>
  </c:if>

  <c:set var="actionUrl">
    <c:choose>
      <c:when test="${course.courseId != null}">
        ${pageContext.request.contextPath}/courses/update/${course.courseId}
      </c:when>
      <c:otherwise>${pageContext.request.contextPath}/courses/save</c:otherwise>
    </c:choose>
  </c:set>

  <div class="form-card">
    <form:form method="post" action="${actionUrl}" modelAttribute="course">

      <div class="form-row">
        <div class="form-group">
          <label class="form-label" for="courseCode">Course Code</label>
          <form:input path="courseCode" id="courseCode" cssClass="form-control"
                      placeholder="e.g. CS101"/>
          <form:errors path="courseCode" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <label class="form-label" for="credits">Credits</label>
          <form:input path="credits" id="credits" type="number" cssClass="form-control"
                      min="1" max="6" placeholder="1-6"/>
          <form:errors path="credits" cssClass="form-error"/>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label" for="courseName">Course Name</label>
        <form:input path="courseName" id="courseName" cssClass="form-control"
                    placeholder="e.g. Introduction to Computer Science"/>
        <form:errors path="courseName" cssClass="form-error"/>
      </div>

      <div class="form-group">
        <label class="form-label" for="department">Department</label>
        <form:input path="department" id="department" cssClass="form-control"
                    placeholder="e.g. Computer Science"/>
        <form:errors path="department" cssClass="form-error"/>
      </div>

      <div style="display:flex;gap:1rem;margin-top:1.5rem">
        <button type="submit" class="btn btn-primary" style="flex:1">
          <c:choose>
            <c:when test="${course.courseId != null}">💾 Update Course</c:when>
            <c:otherwise>✅ Add Course</c:otherwise>
          </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/courses" class="btn btn-secondary">Cancel</a>
      </div>

    </form:form>
  </div>
</div>
</body>
</html>
