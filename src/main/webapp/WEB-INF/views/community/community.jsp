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
    String content2Value = "content";
    String content2Text = "내용";

    pageContext.setAttribute("content1Value", content1Value);
    pageContext.setAttribute("content1Text", content1Text);
    pageContext.setAttribute("content2Value", content2Value);
    pageContext.setAttribute("content2Text", content2Text);
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" as="style" crossorigin href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.8/dist/web/variable/pretendardvariable-dynamic-subset.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/community.css">
</head>
<body>
    <div class="content">
        <%-- ================ < search bar >================ --%>
        <form class="search" action="" method="get">
            <select class="dropdown" name="type">
                <option value="all" selected>전체</option>
                <option value="${content1Value}">${content1Text}</option>
                <option value="${content2Value}">${content2Text}</option>
            </select>
            <input class="search-text" type="text" placeholder="검색어를 입력하세요.">
            <button class="search-btn" type="submit" onclick="alert('클릭!')"></button>
        </form>
        <%-- ============================================ --%>

        <%-- ================ < inventory >================ --%>
            <div class ="inventory">
                <div>

                </div>
                <hr/>
            </div>
        <%-- ============================================ --%>
    </div>

    ${count}
</body>
</html>
