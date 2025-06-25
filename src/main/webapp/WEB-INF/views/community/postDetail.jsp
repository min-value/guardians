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
    const loginUserPk = '${sessionScope.loginUser != null ? sessionScope.loginUser.userPk : -1}';
    const postPk = '${post.post_pk}';
</script>
<script src="${pageContext.request.contextPath}/assets/js/community/loadComment.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/community/commentSubmit.js"></script>
</head>
<body>
    <%@ include file="../include/header.jsp" %>
    <div class="backgroundWrapper">
        <%@ include file="../include/headerImg.jsp" %>
    </div>
    <div class="content">
        <div class="goPrev" onclick="goPrevPage()">
            <div class="prevImg"></div>
            <div class="prevText">글 목록</div>
        </div>
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
        <div id="write-comment">
            <form id="commentForm" action="/community/comment/add" method="post" onsubmit="return checkLogin();">
                <textarea id="write" name="c_content" type="text" placeholder="서로를 존중하는 댓글 문화를 만들어주세요."></textarea>
                <input type="hidden" name="post_pk" value=${post.post_pk}>
                <input type="hidden" name="user_pk" value=${sessionScope.loginUser.userPk}>
                <button id="sendBtn" type="submit" data-login="${not empty sessionScope.loginUser}">댓글작성</button>
            </form>
        </div>
        <div id="pagination"></div>
    </div>
    <%@ include file="../include/footer.jsp" %>
<form id="deleteForm" action="/community/post/delete" method="post">
    <input type="hidden" name="post_pk" value="${post.post_pk}">
</form>
<div id="deleteComment"></div>
<script>
    function toggleDropdown() {
        const dropdown = document.getElementById("edit-dropdown-wrapper");
        dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
    }

    function goPrevPage(){
        location.href='/community/post';
    }

    function editPost() {
        location.href='/community/post/${post.post_pk}/modify';
    }

    function deletePost() {
        if (confirm("정말 삭제하시겠습니까?")) {
            document.getElementById("deleteForm").submit();
        }
    }

    function checkLogin(){
        let find = document.commentForm;
        const isLoggedIn = document.querySelector("#sendBtn").dataset.login === 'true';
        const commentContent = document.getElementById("write").value.trim();

        if(!isLoggedIn){
            alert("로그인 후 댓글을 작성하실 수 있습니다.");
            return false;
        }

        if(commentContent === ""){
            alert("내용을 작성하세요");
            return false;
        }

        return true;
    }
</script>
</body>
</html>
