<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/assets/css/include/header.css">

<header class="header">
    <div class="login-wrapper">
        <div class="login">
            <c:if test="${empty sessionScope.loginUser}">
                <a href="${pageContext.request.contextPath}/user/login">로그인</a>
                <span class="divider">|</span>
                <a href="${pageContext.request.contextPath}/user/signup">회원가입</a>
            </c:if>
            <c:if test="${not empty sessionScope.loginUser}">
                <a href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
                <span class="divider">|</span>
                <a href="${pageContext.request.contextPath}/user/mypage">마이페이지</a>
            </c:if>
        </div>
    </div>

    <div class="menu-wrapper">
        <div class="header-inner">
            <ul class="menu-list">
                <li><a href="/story/videos">STORY</a></li>
                <li><a href="/teaminfo/club">TEAMINFO</a></li>
                <li><a href="/games/all">GAME</a></li>
                <li><a href="/tickets/all">TICKET</a></li>
                <li><a href="/community/post">COMMUNITY</a></li>
                <li><a href="/fairy">FAIRY</a></li>
            </ul>

            <div class="logo">
                <a href="/home">
                    <img src="/assets/img/header/header-logo.png" alt="로고">
                </a>
            </div>
        </div>
    </div>

    <!-- 세부 메뉴 (hover 시 열림) -->
    <div class="depth2-background">
        <div class="depth2-wrapper">
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="/story/videos">하이라이트</a></li>
                    <li><a href="/story/news">뉴스 스크랩</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="/teaminfo/club">구단 소개</a></li>
                    <li><a href="#">경기장 소개</a></li>
                    <li><a href="#">스폰서</a></li>
                    <li><a href="#">선수 정보</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="/games/all">경기 일정</a></li>
                    <li><a href="/games/details">경기 결과</a></li>
                    <li><a href="/games/rank">순위</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="/tickets/all">예매</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="/community/post">커뮤니티</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="/fairy">승리요정</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>
