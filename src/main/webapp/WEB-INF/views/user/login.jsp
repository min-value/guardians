<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>로그인</title>
    <link rel="stylesheet" href="/assets/css/user/login.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/include/header.jsp" %>
    <div class="header-title" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
        <!--나중에 컴포넌트로 바꿀 것.-->
        <img src="/assets/img/header/header-title.png" alt="헤더 타이틀" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
    </div>
    <div class="login-form">
        <form action="login" method="post" id="board1" onsubmit="return loginCheck();">
            <div class="input-form">
                <div class="login-input">
                    <label for="id">아이디</label>
                    <input type="text" id="id" name="id" placeholder="아이디를 입력하세요">
                </div>
                <div class="login-input">
                    <label for="pwd">비밀번호</label>
                    <input type="password" id="pwd" name="pwd" placeholder="비밀번호를 입력하세요">
                </div>
                <div class="login-btn"><input type="submit" value="로그인" alt="로그인"></div>
                <div class="signup">
                    <span>계정이 없으신가요?</span>
                    <a class="signup-link" href="/signup.do">회원가입</a>
                </div>
            </div>
        </form>
        <hr class="divider-line" />
        <div class="kakao-login">
            <button type="button" class="kakao-btn">
                <img src="/assets/img/user/kakao-logo.svg" alt="카카오 로고" class="kakao-logo" />
                카카오로 시작하기
            </button>
        </div>
    </div>
</body>
</html>
