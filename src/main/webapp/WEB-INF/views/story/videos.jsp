<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String content1Value = "title";
    String content1Text = "제목";
    String content2Value = "content";
    String content2Text = "내용";
    String pageTitle = "하이라이트";
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/story/videos.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/story/video/loadVideo.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
</head>
<body>
    <%@ include file="../include/header.jsp" %>
    <%@ include file="../include/headerImg.jsp" %>

    <div class="content">
        <%@ include file="../include/search.jsp" %>

        <div id="videos-list"></div>

        <div id="pagination"></div>
    </div>

    <%@ include file="../include/footer.jsp" %>
</body>
</html>
