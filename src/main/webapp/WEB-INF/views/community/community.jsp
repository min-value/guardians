<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/community.css">
<%--    <link rel="stylesheet" href="/assets/css/include/postList.css">--%>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
<script>
    const loginUserName = '${sessionScope.loginUser.userName}';
</script>
<script src="${pageContext.request.contextPath}/assets/js/community/loadPage.js"></script>
</head>
<body>
    <%@ include file="../include/header.jsp" %>
    <div class="backgroundWrapper">
        <%@ include file="../include/headerImg.jsp" %>
    </div>
    <div class="content">
        <%@ include file="../include/search.jsp" %>

        <%-- ================ < inventory >================ --%>
            <div class ="inventory">
                <div id="inventoryList">
                    <div class ="all"><span id="all-posts-btn" class="clickable">전체</span></div>
                    <c:if test="${not empty sessionScope.loginUser}">
                        <div id="my-posts-btn" class="my"><span class="clickable">내가 쓴 글</span></div>
                    </c:if>
                    <div class ="else"></div>
                </div>
                <hr/>
                <div class="postHeader">
                    <div class="title">제목</div>
                    <div class="writer">작성자</div>
                    <div class="date">작성일시</div>
                </div>
                <div id="post-container"></div>
            </div>
        <c:if test="${not empty sessionScope.loginUser}">
            <div class="btnLocation">
                <button class="writeBtn" type="button">글쓰기</button>
            </div>
        </c:if>
        <div id="pagination"></div>

        <%-- ============================================ --%>
    </div>
    <div class="footer"></div>
    <%@ include file="../include/footer.jsp" %>

<%--<form id="filterForm" action="/community/post" method="get">--%>
<%--    <input type="hidden" name="type" value="">--%>
<%--    <input type="hidden" name="keyword" value="">--%>
<%--    <input type="hidden" name="page" value="1">--%>
<%--</form>--%>
<%--<script>--%>
<%--    function showAllList(){--%>
<%--        const form = document.getElementById("filterForm");--%>
<%--        form.type.value = '';--%>
<%--        form.keyword.value = '';--%>
<%--        form.page.value = 1;--%>
<%--        form.submit();--%>
<%--    }--%>

<%--    function showMyList() {--%>
<%--        const form = document.getElementById("filterForm");--%>
<%--        form.type.value = 'writer';--%>
<%--        form.keyword.value = '${sessionScope.loginUser.userName}';--%>
<%--        form.page.value = 1;--%>
<%--        form.submit();--%>
<%--    }--%>
<%--</script>--%>
</body>
</html>
