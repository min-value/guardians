<%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 25. 6. 19.
  Time: 오전 9:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String content1Value = "title";
    String content1Text = "제목";
    String content2Value = "writer";
    String content2Text = "작성자";
    String pageTitle = "커뮤니티";

    pageContext.setAttribute("content1Value", content1Value);
    pageContext.setAttribute("content1Text", content1Text);
    pageContext.setAttribute("content2Value", content2Value);
    pageContext.setAttribute("content2Text", content2Text);
    pageContext.setAttribute("pageTitle", pageTitle);
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" as="style" crossorigin href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.8/dist/web/variable/pretendardvariable-dynamic-subset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/community.css">
</head>
<body>
    <%@ include file="../include/header.jsp" %>
    <%@ include file="../include/headerImg.jsp" %>
    <div class="content">
        <%@ include file="../include/search.jsp" %>

        <%-- ================ < inventory >================ --%>
            <div class ="inventory">
                <div class ="inventoryText"></div>
                <hr/>
            </div>
        <%-- ============================================ --%>

        ${count}
    </div>

    <%@ include file="../include/footer.jsp" %>
</body>
</html>
