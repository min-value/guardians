<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/games/rank.css">
    <link rel="stylesheet" href="/assets/css/font.css">
</head>
<body>
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<div class="header-title">
    <img src="/assets/img/header/header-title.png" alt="헤더">
    <div style="position: absolute; color: black; font-size: 48px; font-weight: 600;">
        순위
    </div>
</div>

<h1>2025 KBO 리그</h1>

<div class="rank-container">
    <table class="rank-table">
        <thead>
        <tr>
            <th>순위</th>
            <th>팀명</th>
            <th>승률</th>
            <th>게임차</th>
            <th>승</th>
            <th>무</th>
            <th>패</th>
            <th>경기</th>
            <th>연속</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="team" items="${teamList}">
            <tr>
                <td>${team.ranking}</td>
                <td>
                    <c:choose>
                        <c:when test="${team.teamName eq '신한'}">
                            <img src="/assets/img/teamlogos/6.svg" alt="신한">
                        </c:when>
                        <c:when test="${team.teamName eq '삼성'}">
                            <img src="/assets/img/teamlogos/5.svg" alt="삼성">
                        </c:when>
                        <c:when test="${team.teamName eq '키움'}">
                            <img src="/assets/img/teamlogos/10.svg" alt="키움">
                        </c:when>
                        <c:when test="${team.teamName eq '롯데'}">
                            <img src="/assets/img/teamlogos/3.svg" alt="롯데">
                        </c:when>
                        <c:when test="${team.teamName eq 'LG'}">
                            <img src="/assets/img/teamlogos/2.svg" alt="LG">
                        </c:when>
                        <c:when test="${team.teamName eq 'NC'}">
                            <img src="/assets/img/teamlogos/8.svg" alt="NC">
                        </c:when>
                        <c:when test="${team.teamName eq '두산'}">
                            <img src="/assets/img/teamlogos/9.svg" alt="두산">
                        </c:when>
                        <c:when test="${team.teamName eq 'KT'}">
                            <img src="/assets/img/teamlogos/7.svg" alt="KT">
                        </c:when>
                        <c:when test="${team.teamName eq '한화'}">
                            <img src="/assets/img/teamlogos/1.svg" alt="한화">
                        </c:when>
                        <c:when test="${team.teamName eq 'KIA'}">
                            <img src="/assets/img/teamlogos/4.svg" alt="KIA">
                        </c:when>
                    </c:choose>
                    <span class="team-name-text">${team.teamName}</span>
                </td>
                <td><fmt:formatNumber value="${team.winRate}" pattern="0.000"/></td>
                <td>${team.behind}</td>
                <td>${team.win}</td>
                <td>${team.draw}</td>
                <td>${team.lose}</td>
                <td>${team.gameCnt}</td>
                <td>${team.winStreak}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div>
    <%@ include file="/WEB-INF/views/include/footer.jsp" %>
</div>

</body>
</html>
