<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String pageTitle = "커뮤니티";
    pageContext.setAttribute("pageTitle", pageTitle);
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/paginationForComment.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/postDetail.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
<script>
    function loadComment(page){
        const postPk = window.location.pathname.split('/').pop();
        $.ajax({
            url:'/community/comment',
            method: 'GET',
            data:{
                post_pk: postPk,
                page: page
            },
            success: function (res){
                const commentList = res.list;
                const totalCount = res.totalCount;
                const container = $('#comment-container').empty();
                commentList.forEach(comment =>{
                    const date = new Date(comment.c_date);
                    const formattedDate = `\${date.getFullYear()}-\${(date.getMonth()+1).toString().padStart(2,'0')}-\${date.getDate().toString().padStart(2,'0')} \${date.getHours().toString().padStart(2,'0')}:\${date.getMinutes().toString().padStart(2,'0')}`;
                    container.append(`
                        <div class="comment-box">
                            <div class="comment-header">
                                <span class="comment-user">${'${comment.user_name}'}</span>
                                <span class="comment-date">\${formattedDate}</span>
                                <div class="comment-actions">
                                    <button class="comment-edit">수정</button>
                                    <button class="comment-delete">삭제</button>
                                </div>
                            </div>
                            <div class="comment-content">
                                ${'${comment.c_content}'}
                            </div>
                        </div>
                    `);
                });
                if (totalCount > 0) {
                    createPagination({
                        currentPage: page,
                        totalCount: totalCount,
                        onPageChange: (newPage) => loadPage(newPage),
                        pageSize: 5,
                        containerId: '#pagination'
                    });
                }
            }
        });
    }
    $(document).ready(()=>{
        loadComment(1);
    });
</script>
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
            <button class="edit">&nbsp</button>
        </div>
        <div id="post-container">
            ${post.p_content}
        </div>
        <div id="comment-container">
            <div class="comment-box">
<%--                <div class="comment-header">--%>
<%--                    <span class="comment-user">userId1</span>--%>
<%--                    <span class="comment-date">2025-06-15 9:21</span>--%>
<%--                    <div class="comment-actions">--%>
<%--                        <button class="comment-edit">수정</button>--%>
<%--                        <button class="comment-delete">삭제</button>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="comment-content">--%>
<%--                    야;; 니눈에는 저게 잘한거냐? 잘한게 있어야 칭찬하지--%>
<%--                </div>--%>
            </div>
        </div>
        <div id="pagination"></div>
    </div>
    <div class="footer"></div>
    <%@ include file="../include/footer.jsp" %>
</body>
</html>
