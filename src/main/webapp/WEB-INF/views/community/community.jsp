<%--
  Created by IntelliJ IDEA.
  User: 82103
  Date: 25. 6. 19.
  Time: 오전 9:26
  To change this template use File | Settings | File Templates.
--%>
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
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/community.css">
    <link rel="stylesheet" href="/assets/css/include/postList.css">
<script>
    function loadPage(page){
        $.ajax({
            url:'/community/page',
            method: 'GET',
            data: {page: page},
            success: function(res){
                const list = res.list;
                const totalCount = res.totalCount;

                const container = $('#post').empty();
                list.forEach(post => {
                    container.append(`
                        <div class="title">${post.title}</div>
                        <div class="writer">${post.user_name}</div>
                        <div class="date"><fmt:formatDate value="${post.p_date}" pattern="yyyy-MM-dd HH:mm" /></div>`
                    );
                });

                const pageCount = Math.ceil(totalCount/10);
                const pagination = $('#pagination').empty();
                for(let i=1; i<=pageCount; i++){
                    pagination.append(`<button onclick="loadPage(${i})">${i}</button>`);
                }
            }
        });
    }

    $(document).ready(()=>{
        loadPage(1);
    });
</script>

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
                <div class="inventoryList">
                    <div class ="all"><span class="clickable" onclick="showPostList()">전체</span></div>
                    <div class ="my"><span class="clickable" onclick="showMyList()">내가 쓴 글</span></div>
                    <div class ="else"></div>
                </div>
                <hr/>
                <div class="postHeader">
                    <div class="title">제목</div>
                    <div class="writer">작성자</div>
                    <div class="date">작성일시</div>
                </div>
                <c:forEach var="post" items="${list}">
                    <div class="post">
                        <div class="title">${post.title}</div>
                        <div class="writer">${post.user_name}</div>
                        <div class="date"><fmt:formatDate value="${post.p_date}" pattern="yyyy-MM-dd HH:mm" /></div>
                    </div>
                </c:forEach>
            </div>
        <div class="btnLocation">
            <button class="writeBtn" type="button">글쓰기</button>
        </div>
        <%-- ============================================ --%>
    </div>
    <div id="pagination"></div>

    <div class="footer"></div>
    <%@ include file="../include/footer.jsp" %>

<script>
    function showPostList() {
        document.querySelector('.postList').style.display = 'table';
        document.querySelector('.myList').style.display = 'none';
        document.querySelector('.inventoryList .all').style.color = 'black';
        document.querySelector('.inventoryList .my').style.color = 'var(--gray-03)';
    }

    function showMyList() {
        document.querySelector('.postList').style.display = 'none';
        document.querySelector('.myList').style.display = 'table';
        document.querySelector('.inventoryList .all').style.color = 'var(--gray-03)';
        document.querySelector('.inventoryList .my').style.color = 'black';
    }
</script>


</body>
</html>
