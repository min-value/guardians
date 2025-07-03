<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>신한 가디언즈</title>
  <link rel="stylesheet" href="/assets/css/colors.css"/>
  <link rel="stylesheet" href="/assets/css/admin/admin.css"/>
</head>

<body>
<%request.setAttribute("activePage", "main");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="container">
  <div class="logout" onclick="location.href='/user/logout';">
    <span class="logout-text">로그아웃</span>
    <img src="/assets/img/icon/logout.png" alt="icon"/>
  </div>
  <div class="box">
    <img class="symbol" src="/assets/img/symbol.png"/>
    <div class="logo">
      <img class="guardians" src="/assets/img/guardians.png"/>
      <input class="main" type="button" value="main" onclick="location.href='/home'">
    </div>
  </div>
</div>

</body>
</html>
