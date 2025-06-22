<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/assets/css/include/header.css">

<header class="header">
    <div class="login-wrapper">
        <div class="login">
            <c:if test="${empty login}">
                <a href="/login.do">로그인</a>
                <span class="divider">|</span>
                <a href="/signup.do">회원가입</a>
            </c:if>
            <c:if test="${!empty login}">
                <a href="/logout.do">로그아웃</a>
                <span class="divider">|</span>
                <a href="/mypage.do">마이페이지</a>
            </c:if>
        </div>
    </div>

    <div class="menu-wrapper">
        <div class="header-inner">
            <ul class="menu-list">
                <li><a href="#">STORY</a></li>
                <li><a href="#">TEAMINFO</a></li>
                <li><a href="#">GAME</a></li>
                <li><a href="#">TICKET</a></li>
                <li><a href="#">FAIRY</a></li>
                <li><a href="#">COMMUNITY</a></li>
            </ul>

            <div class="logo">
                <a href="/index.do">
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
                    <li><a href="#">하이라이트</a></li>
                    <li><a href="#">뉴스 스크랩</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="#">구단 소개</a></li>
                    <li><a href="#">경기장 소개</a></li>
                    <li><a href="#">스폰서</a></li>
                    <li><a href="#">선수 정보</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="#">경기 일정</a></li>
                    <li><a href="#">경기 결과</a></li>
                    <li><a href="#">총 전적</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="#">예매</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="#">승리요정</a></li>
                </ul>
            </div>
            <div class="depth2-column">
                <ul class="depth2">
                    <li><a href="#">커뮤니티</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>
