<%--
  Created by IntelliJ IDEA.
  User: hyesung
  Date: 25. 6. 23.
  Time: 오전 10:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String pageTitle = "티켓";
  pageContext.setAttribute("pageTitle", pageTitle);
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
</head>
<body>
  <%@ include file="../include/header.jsp" %>
  <div class="backgroundWrapper">
    <%@ include file="../include/headerImg.jsp" %>
  </div>
  <div class="filter">
    <form method="get" action="/tickets/all">
      <select name="status" onchange="this.form.submit()">
        <option value="0" <c:if test="${selectedStatus == '0' || empty selectedStatus}">selected</c:if>>전체 일정</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>이번 주</option>
        <option value="2" <c:if test="${selectedStatus == '2'}">selected</c:if>>경기 예정</option>
      </select>
    </form>
  </div>
  <div class="filter">
    <form method="get" action="/tickets/all">
      <select name="status" onchange="this.form.submit()">
        <option value="0" <c:if test="${selectedStatus == '0' || empty selectedStatus}">selected</c:if>>전체 팀</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>신한 가디언즈</option>
      </select>
    </form>
  </div>
  <div class="filter">
    <form method="get" action="/tickets/all">
      <select name="status" onchange="this.form.submit()">
        <option value="0" <c:if test="${selectedStatus == '0' || empty selectedStatus}">selected</c:if>>전체 예매</option>
        <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>예매 중</option>
        <option value="2" <c:if test="${selectedStatus == '2'}">selected</c:if>>매진</option>
      </select>
    </form>
  </div>
  <div>

  </div>
</body>
</html>
