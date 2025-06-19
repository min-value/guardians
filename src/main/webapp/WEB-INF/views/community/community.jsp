<%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 25. 6. 19.
  Time: 오전 9:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/community.css">
</head>
<body>
    <form class="search" action="" method="get">
        <select class="dropdown">
            <option value="all" selected>전체</option>
            <option value="title">제목</option>
            <option value="content">내용</option>
        </select>
        <input class="search-text" type="text" placeholder="검색어를 입력하세요.">
        <button class="search-btn" type="submit" onclick="alert('클릭!')"></button>
    </form>
    <div class="check"></div>
    ${count}
</body>
</html>
