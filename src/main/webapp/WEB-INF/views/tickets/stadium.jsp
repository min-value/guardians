<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
</head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/stadium.css" />
<body>
<button id="back">back</button>
<div class="stadium-container">
  <div class="btn-container">
    <div class="btn-wrapper gobackBtn-wrapper">
      <img src="${pageContext.request.contextPath}/assets/img/tickets/gobackBtn.png" alt="뒤로가기 버튼">
    </div>
    <div class="btn-wrapper enlargementBtn-wrapper">
      <img src="${pageContext.request.contextPath}/assets/img/tickets/enlargementBtn.png" alt="확대 버튼">
    </div>
    <div class="btn-wrapper reductionBtn-wrapper">
      <img src="${pageContext.request.contextPath}/assets/img/tickets/reductionBtn.png" alt="축소 버튼">
    </div>
    <div class="btn-wrapper reloadBtn-wrapper">
      <img src="${pageContext.request.contextPath}/assets/img/tickets/reloadBtn.png" alt="새로고침 버튼">
    </div>
  </div>
  <div class="stadium-wrapper">
    <object id="svgMap" type="image/svg+xml" data="${pageContext.request.contextPath}/assets/img/tickets/ballpark.svg"></object>
  </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/tickets/stadium.js"></script>
</body>
</html>
