<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/postDetail.css">
</head>
<body>
    <%@ include file="../include/header.jsp" %>
    <div class="backgroundWrapper">
        <%@ include file="../include/headerImg.jsp" %>
    </div>
    <div class="content">
        <div class="postHeader">
            <div class="title">${post.title}</div>
            <div class="writer">${post.user_name}</div>
            <div class="date">
                <fmt:formatDate value="${post.p_date}" pattern="yyyy-MM-dd HH:mm" />
            </div>
        </div>
        <div id="post-container">
            ${post.p_content}
        </div>
        <div id="comment-container"></div>
    </div>
    <div class="footer"></div>
    <%@ include file="../include/footer.jsp" %>
</body>
</html>
