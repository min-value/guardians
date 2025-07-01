<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/include/header.jsp" />
<html>
<head>
    <title>상세결과</title>
    <link rel="stylesheet" href="/assets/css/games/details.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/assets/js/games/details.js"></script>
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

    <!-- 경기 카드 반복 -->
    <div id="detail-box" class="detail-box">
        <c:forEach var="game" items="${detailList}">
            <!-- 결과 클래스 -->
            <c:set var="ourResultClass" value="" />
            <c:set var="oppResultClass" value="" />
            <c:choose>
                <c:when test="${game.result eq 'WIN'}">
                    <c:set var="ourResultClass" value="win" />
                    <c:set var="oppResultClass" value="lose" />
                </c:when>
                <c:when test="${game.result eq 'LOSE'}">
                    <c:set var="ourResultClass" value="lose" />
                    <c:set var="oppResultClass" value="win" />
                </c:when>
                <c:when test="${game.result eq 'DRAW'}">
                    <c:set var="ourResultClass" value="draw" />
                </c:when>
            </c:choose>

            <c:set var="myPitcher" value="" />
            <c:set var="oppPitcher" value="" />
            <c:set var="myResultText" value="" />
            <c:set var="oppResultText" value="" />
            <c:choose>
                <c:when test="${game.result eq 'WIN'}">
                    <c:set var="myPitcher" value="${game.winPitcher}" />
                    <c:set var="oppPitcher" value="${game.losePitcher}" />
                    <c:set var="myResultText" value="승" />
                    <c:set var="oppResultText" value="패" />
                </c:when>
                <c:when test="${game.result eq 'LOSE'}">
                    <c:set var="myPitcher" value="${game.losePitcher}" />
                    <c:set var="oppPitcher" value="${game.winPitcher}" />
                    <c:set var="myResultText" value="패" />
                    <c:set var="oppResultText" value="승" />
                </c:when>
                <c:when test="${game.result eq 'DRAW'}">
                    <c:set var="myPitcher" value="-" />
                    <c:set var="myResultText" value="무" />
                </c:when>
            </c:choose>

            <fmt:formatDate value="${game.date}" pattern="MM-dd" var="formattedDate" />

            <!-- 카드 -->
            <div class="game-card">
                <div class="summary ${ourResultClass}">
                    <div class="summary-header">
                        <div class="pitcher-with-result">
                            <div class="game-result-circle ${ourResultClass}">${myResultText}</div>
                            <div class="game-pitcher">${myPitcher}</div>
                        </div>
                        <img class="team-logo" src="/assets/img/games/6.png" alt="ourlogo" />
                        <div class="game-score">${game.ourScore}</div>

                        <div class="vs-block">
                            <div class="vs-text">VS</div>
                            <div class="vs-date">${formattedDate}</div>
                        </div>

                        <div class="game-score">${game.oppScore}</div>
                        <img class="team-logo" src="/assets/img/games/${game.oppTeamPk}.png" alt="opplogo" />

                        <div class="pitcher-with-result"
                             style="${game.result eq 'DRAW' ? 'visibility: hidden;' : ''}">
                            <div class="game-result-circle ${oppResultClass}">${oppResultText}</div>
                            <div class="game-pitcher">${oppPitcher}</div>
                        </div>
                    </div>
                    <img class="summary-chevron" src="/assets/img/icon/chevron-down.svg" alt="open-detail">
                </div>

                <!-- 상세 -->
                <div class="detail" style="display: none;">
                    <!-- 그래프용 데이터 텍스트 -->
                    <div class="hit">안타: ${game.ourHit} / ${game.oppHit}</div>
                    <div class="homerun">홈런: ${game.ourHomerun} / ${game.oppHomerun}</div>
                    <div class="strike-out">삼진: ${game.ourStrikeOut} / ${game.oppStrikeOut}</div>
                    <div class="bb">사사구: ${game.ourBb} / ${game.oppBb}</div>
                    <div class="miss">실책: ${game.ourMiss} / ${game.oppMiss}</div>

                    <!-- 팀명 -->
                    <div class="team-name">
                        <div class="our-team-name">신한</div>
                        <div class="vs">VS</div>
                        <div class="opp-team-name">${game.teamName}</div>
                    </div>

                    <!-- 그래프 출력 -->
                    <div class="custom-bar-chart"></div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>
