<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/header.jsp" />
<html>
<head>
    <title>상세결과</title>
    <link rel="stylesheet" href="/assets/css/games/details.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/assets/js/games/details.js"></script>
    <script src="/assets/js/include/pagination.js"></script>
</head>
<body>
<div class="header-title">
    <img src="/assets/img/header/header-title.png" alt="헤더">
    <div style="position: absolute; color: black; font-size: 48px; font-weight: 600;">
        경기 결과
    </div>
</div>

<div class="details-wrapper">
    <!-- 드롭다운 -->
    <div class="dropdown-wrapper">
        <div class="select-wrapper">
            <select id="year-select" class="year-select">
                <c:forEach var="y" begin="2023" end="2025">
                    <option value="${y}" <c:if test="${y == selectedYear}">selected</c:if>>${y}년</option>
                </c:forEach>
            </select>
            <img src="/assets/img/icon/dropdownVector.png" class="dropdown-icon">
        </div>
        <div class="select-wrapper">
            <select id="month-select" class="month-select">
                <c:forEach var="m" begin="1" end="12">
                    <c:set var="mm" value="${m lt 10 ? '0' : ''}${m}" />
                    <option value="${mm}" <c:if test="${m == selectedMonth}">selected</c:if>>${mm}월</option>
                </c:forEach>
            </select>
            <img src="/assets/img/icon/dropdownVector.png" class="dropdown-icon">
        </div>
    </div>

    <!-- AJAX로 경기 카드 들어갈 곳 -->
    <div id="detail-box" class="detail-box"></div>

    <!-- 페이지네이션 들어갈 곳 -->
    <div id="pagination"></div>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>
