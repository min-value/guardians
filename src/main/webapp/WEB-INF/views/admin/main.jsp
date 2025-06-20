<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
  <link rel="stylesheet" href="/assets/css/colors.css"/>
  <link rel="stylesheet" href="/assets/css/admin/admin.css"/>
</head>

<body>
<%request.setAttribute("activePage", "main");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="box">
  <img class="symbol" src="/assets/img/symbol.png"/>
  <div class="logo">
    <img class="guardians" src="/assets/img/guardians.png"/>
    <input class="main" type="button" value="main" onclick="location.href='/index.do'">
  </div>
</div>
</body>
</html>
