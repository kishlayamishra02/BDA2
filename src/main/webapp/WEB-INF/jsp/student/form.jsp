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
    <a href="${pageContext.request.contextPath}/students" class="nav-link active">Students</a>
    <a href="${pageContext.request.contextPath}/courses"  class="nav-link">Courses</a>
  </div>
</nav>

<div class="container">
  <div class="page-header">
    <h1 class="page-title">${pageTitle}</h1>
    <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary">← Back</a>
  </div>

  <c:if test="${not empty errorMsg}">
    <div class="alert alert-danger">❌ ${errorMsg}</div>
  </c:if>

  <%-- Determine action URL dynamically for create vs. update --%>
  <c:set var="actionUrl">
    <c:choose>
      <c:when test="${student.studentId != null}">
        ${pageContext.request.contextPath}/students/update/${student.studentId}
      </c:when>
      <c:otherwise>${pageContext.request.contextPath}/students/save</c:otherwise>
    </c:choose>
  </c:set>

  <div class="form-card">
    <form:form method="post" action="${actionUrl}" modelAttribute="student">

      <div class="form-row">
        <div class="form-group">
          <label class="form-label" for="firstName">First Name</label>
          <form:input path="firstName" id="firstName" cssClass="form-control"
                      placeholder="e.g. Alice"/>
          <form:errors path="firstName" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <label class="form-label" for="lastName">Last Name</label>
          <form:input path="lastName" id="lastName" cssClass="form-control"
                      placeholder="e.g. Johnson"/>
          <form:errors path="lastName" cssClass="form-error"/>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label" for="email">Email Address</label>
        <form:input path="email" id="email" type="email" cssClass="form-control"
                    placeholder="student@university.edu"/>
        <form:errors path="email" cssClass="form-error"/>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label class="form-label" for="dateOfBirth">Date of Birth</label>
          <form:input path="dateOfBirth" id="dateOfBirth" type="date" cssClass="form-control"/>
          <form:errors path="dateOfBirth" cssClass="form-error"/>
        </div>
        <div class="form-group">
          <label class="form-label" for="major">Major</label>
          <form:input path="major" id="major" cssClass="form-control"
                      placeholder="e.g. Computer Science"/>
          <form:errors path="major" cssClass="form-error"/>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label" for="course">Enroll in Course</label>
        <form:select path="course" id="course" cssClass="form-control">
          <form:option value="" label="-- Select a Course --"/>
          <form:options items="${courses}" itemValue="courseId"
                        itemLabel="courseName"/>
        </form:select>
        <form:errors path="course" cssClass="form-error"/>
      </div>

      <div style="display:flex;gap:1rem;margin-top:1.5rem">
        <button type="submit" class="btn btn-primary" style="flex:1">
          <c:choose>
            <c:when test="${student.studentId != null}">💾 Update Student</c:when>
            <c:otherwise>✅ Enroll Student</c:otherwise>
          </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/students" class="btn btn-secondary">Cancel</a>
      </div>

    </form:form>
  </div>
</div>
</body>
</html>
