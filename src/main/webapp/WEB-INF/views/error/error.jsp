<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/error/error.css">
</head>
<body>
    <div class="container">
      <div class="error-body">
        <div class="title">
          이용에 불편을 드려 죄송합니다
        </div>
        <img src="${pageContext.request.contextPath}/assets/img/header/header-logo.png" alt="logo" class="logo">
        <div class="text">
          현재 일시적인 오류이거나 요청하신 페이지를 찾을 수 없습니다. <br>
          빠른 시간 내에 처리하도록 하겠습니다.<br>
          사이트 이용에 불편을 드려 죄송합니다.
        </div>
        <input type="button" value="메인 페이지" onclick="location.href='/home';" class="btn">
      </div>
    </div>
</body>
</html>
