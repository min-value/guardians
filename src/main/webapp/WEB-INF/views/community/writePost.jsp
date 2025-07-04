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
    <%@ include file="../include/header.jsp" %>
    <div class="backgroundWrapper">
        <%@ include file="../include/headerImg.jsp" %>
    </div>
    <div class="content">
        <form id="writePost" action="/community/post/add" method="post" onsubmit="return checkPost();">
            <div class="postHeader">
                <input id="postTitle" class="title" name="title" maxlength="28" placeholder="제목을 입력하세요(최대28자)" autofocus>
                <button id="recommendBtn" type="button" onclick="recommendContent()">AI 내용 추천</button>
            </div>
            <div id="post-container">
                <textarea id="write-post" name="p_content" type="text" row="1"
                  placeholder="※ 선수에 대한 무분별한 비난, 비하, 인신공격성 댓글은 삼가주시기 바랍니다.&#13;&#10;&#13;&#10; SHINHAN가디언즈와 선수들을 응원하는 팬 커뮤니티인 만큼, &#13;&#10;&#13;&#10;서로를 존중하며 건강하고 즐거운 소통 문화를 함께 만들어주세요."></textarea>
            </div>
            <input type="hidden" name="user_pk" value="${sessionScope.loginUser.userPk}">
            <div class="btnLocation">
                <button class="writeBtn" type="submit">글 등록하기</button>
            </div>
        </form>
    </div>
    <%@ include file="../include/footer.jsp" %>
<script>
    function checkPost(){
        const title = document.getElementById("postTitle").value.trim();
        const postContent = document.getElementById("write-post").value.trim();

        if(title === ""){
            alert("제목을 작성하세요");
            return false;
        }
        else if(postContent === ""){
            alert("내용을 작성하세요");
            return false;
        }
        return true;
    }

    function recommendContent(){
        const title = document.getElementById("postTitle").value;

        if(!title){
            alert("제목을 입력하세요");
            return;
        }

        $.ajax({
            url: "/gpt/aiRecommend",
            method: "POST",
            //dataType: 'json',
            //contentType: 'application/json; charset=utf-8',
            //data: JSON.stringify({title}),
            data:{'title':title},
            success: function (res){
                $('#write-post').val(res.recommendation);
            },
            error: function(){
                alert("AI 추천에 실패했습니다.");
            }
        });
    }
</script>
</body>
</html>
