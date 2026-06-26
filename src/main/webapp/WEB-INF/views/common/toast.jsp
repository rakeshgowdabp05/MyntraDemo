<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="flashSuccess" value="${requestScope.successMessage}" />
<c:set var="flashError" value="${requestScope.errorMessage}" />
<c:set var="flashInfo" value="${requestScope.infoMessage}" />

<c:if test="${empty flashSuccess}">
  <c:set var="flashSuccess" value="${sessionScope.successMessage}" />
</c:if>

<c:if test="${empty flashError}">
  <c:set var="flashError" value="${sessionScope.errorMessage}" />
</c:if>

<c:if test="${empty flashInfo}">
  <c:set var="flashInfo" value="${sessionScope.infoMessage}" />
</c:if>

<div
  id="mdToastRoot"
  class="md-toast-root"
  data-success="<c:out value='${flashSuccess}' />"
  data-error="<c:out value='${flashError}' />"
  data-info="<c:out value='${flashInfo}' />"
></div>

<c:remove var="successMessage" scope="session" />
<c:remove var="errorMessage" scope="session" />
<c:remove var="infoMessage" scope="session" />
