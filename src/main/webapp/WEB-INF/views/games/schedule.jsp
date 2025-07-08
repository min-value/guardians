<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String pageTitle = "경기 일정";
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/games/schedule.css">
    <link rel="stylesheet" href="/assets/css/font.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/assets/js/games/schedule.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<%@ include file="../include/headerImg.jsp" %>

<!-- 드롭다운 -->
<div class="calendar-dropdown">
    <!-- 년도 -->
    <div class="dropdown-wrapper">
        <select id="year-select" class="year-select">
            <c:forEach var="y" begin="2023" end="2025">
                <option value="${y}">${y}년</option>
            </c:forEach>
        </select>
        <img src="/assets/img/icon/dropdownVector.svg" class="dropdown-icon">
    </div>

    <!-- 월 -->
    <div class="dropdown-wrapper">
        <select id="month-select" class="month-select">
            <c:forEach var="m" begin="1" end="12">
                <option value="${m}">${m}월</option>
            </c:forEach>
        </select>
        <img src="/assets/img/icon/dropdownVector.svg" class="dropdown-icon">
    </div>
</div>

<!-- 달력 전체 -->
<div class="calendar-wrapper">
    <div id="calendar-placeholder">
        <!-- 요일 -->
        <div class="calendar-grid calendar-weekdays">
            <div class="calendar-header">일</div>
            <div class="calendar-header">월</div>
            <div class="calendar-header">화</div>
            <div class="calendar-header">수</div>
            <div class="calendar-header">목</div>
            <div class="calendar-header">금</div>
            <div class="calendar-header">토</div>
        </div>

        <div class="calendar-grid" id="calendar-grid">
            <%-- 날짜 --%>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>
