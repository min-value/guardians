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
        const loginUserPk = '${sessionScope.loginUser != null ? sessionScope.loginUser.userPk : -1}';
        const postPk = '${post.post_pk}';
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
                    const isMine = (comment.user_pk === Number(loginUserPk));

                    const html = `
                        <div class="comment-box">
                            <div class="comment-header">
                                <span class="comment-user">\${comment.user_name}</span>
                                <span class="comment-date">\${formattedDate}</span>
                                <div class="comment-actions">
                                    \${isMine ? `
                                        <button class="comment-edit">수정</button>
                                        <button class="comment-delete">삭제</button>` : ''}
                                </div>
                            </div>
                            <div class="comment-content">
                                \${comment.c_content}
                            </div>
                        </div>
                    `;

                    container.append(html);
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
            <c:if test="${sessionScope.loginUser.userPk eq post.user_pk}">
                <button class="edit" onclick="toggleDropdown()">&nbsp</button>
                <div id="edit-dropdown-wrapper" style="display: none">
                    <div class="edit-dropdown" id="editDropdown">
                        <div onclick="editPost()">수정</div>
                        <div onclick="deletePost()">삭제</div>
                    </div>
                </div>
            </c:if>
        </div>
        <div id="post-container">
            ${post.p_content}
        </div>
        <div id="comment-container">
            <div class="comment-box"></div>
        </div>
        <div id="pagination"></div>
    </div>
    <div class="footer"></div>
    <%@ include file="../include/footer.jsp" %>
<form id="deleteForm" action="/community/post/delete" method="post">
    <input type="hidden" name="post_pk" value="${post.post_pk}">
</form>
<script>
    function toggleDropdown() {
        const dropdown = document.getElementById("edit-dropdown-wrapper");
        dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
    }

    function editPost() {
        alert("수정 기능 실행!");
        // 여기에 수정 페이지로 이동하거나 수정 모달 띄우는 로직 넣기
    }

    function deletePost() {
        if (confirm("정말 삭제하시겠습니까?")) {
            document.getElementById("deleteForm").submit();
        }
    }
</script>
</body>
</html>
