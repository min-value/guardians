<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String pageTitle = "커뮤니티";
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/writePost.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
</head>
<body>
    <%@ include file="../include/header.jsp" %>ㄴ
    <div class="backgroundWrapper">
        <%@ include file="../include/headerImg.jsp" %>
    </div>
    <div class="content">
        <form id="modifyPost" action="/community/post/${post.post_pk}/modify" method="post">
            <div class="postHeader">
                <input class="title" name="title" value="${post.title}" autofocus>
            </div>
            <div id="post-container">
                <textarea id="write-post" name="p_content" type="text">${post.p_content}</textarea>
            </div>
            <input type="hidden" name="user_pk" value="${sessionScope.loginUser.userPk}">
            <div class="btnLocation">
                <button class="writeBtn" type="submit">글 수정하기</button>
            </div>
        </form>
    </div>
    <%@ include file="../include/footer.jsp" %>
</body>
</html>
