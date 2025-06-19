<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
</head>
<body>
<%request.setAttribute("activePage", "main");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="container">
  <img class="symbol" src="/assets/img/symbol.png"/>
  <div class="logo">
    <img class="guardians" src="/assets/img/guardians.png"/>
    <input class="main" type="button" src="/index.do" value="main">
  </div>
</div>
</body>
</html>
