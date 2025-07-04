<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "로그인";
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/user/login.css">
    <script src="https://developers.kakao.com/sdk/js/kakao.min.js"></script>
    <script>
        Kakao.init('ceead2a16e86d5010c921c46bc602aa6');
        console.log('Kakao SDK 초기화 여부:', Kakao.isInitialized());
    </script>

</head>
<body>
    <%@ include file="/WEB-INF/views/include/header.jsp" %>
    <%@ include file="../include/headerImg.jsp" %>
    <div class="login-form">
        <form action="${pageContext.request.contextPath}/user/login" method="post" id="loginForm">
            <div class="input-form">
                <div class="login-input">
                    <label for="id">아이디</label>
                    <input type="text" id="id" name="userId" placeholder="아이디를 입력하세요" required autofocus>
                </div>
                <div class="login-input">
                    <label for="pwd">비밀번호</label>
                    <input type="password" id="pwd" name="userPwd" placeholder="비밀번호를 입력하세요" required>
                </div>
                <div class="login-btn"><input type="submit" value="로그인" alt="로그인"></div>
                <div class="signup">
                    <span>계정이 없으신가요?</span>
                    <a class="signup-link" href="${pageContext.request.contextPath}/user/signup">회원가입</a>
                </div>
            </div>
        </form>

        <c:if test="${not empty errorMessage}">
            <p style="color: var(--errorColor);">${errorMessage}</p>
        </c:if>

        <hr class="divider-line" />
        <div class="kakao-login">
            <button type="button" class="kakao-btn">
                <img src="/assets/img/user/kakao-logo.svg" alt="카카오 로고" class="kakao-logo" />
                카카오 로그인
            </button>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const btn = document.querySelector('.kakao-btn');
            console.log('found kakao-btn:', btn);
            if (!btn) return;

            const contextPath = "${pageContext.request.contextPath}";
            const redirectUri = window.location.origin + contextPath + '/user/kakao/callback';
            console.log('will use redirectUri:', redirectUri);

            btn.addEventListener('click', function() {
                console.log('kakao-btn clicked');
                Kakao.Auth.authorize({
                    redirectUri: redirectUri,
                    scope: 'profile_nickname, account_email'
                });
            });
        });
    </script>
</body>
</html>
