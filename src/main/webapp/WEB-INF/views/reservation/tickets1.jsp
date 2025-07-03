<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>예매</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets-common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/loading.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<script>
    let map = ${zoneMapDetail}; //구역 별 선점/판매 된 좌석 번호 배열
    let zoneInfo = ${zoneInfo}; //구역 별 정보(pk, name, color, cost, total num, remaining num)

    let gameInfo = JSON.parse('${gameInfoJson}');
    const gamePk = Number(gameInfo['gamePk']);
    localStorage.setItem('gameInfo' + gamePk, JSON.stringify(gameInfo));

    let discountInfo = JSON.parse('${discountInfo}');
    localStorage.setItem('discountInfo' + gamePk, JSON.stringify(discountInfo));

    const available = `${available}`;
    console.log('예매 가능 장수: ' + available);
</script>
<!-- 등급/좌석 선택 -->
<div class="full-container">
    <jsp:include page="topbar.jsp" flush="false">
        <jsp:param name="type" value="1"/>
    </jsp:include>
    <div class="mainback-container">
        <div class="main-container">
            <div class="left-container">
                <div class="left-wrapper">
                    <div class="gameinfo-container">
                        <div class="teams-wrapper">
                            <div id="team1">신한 가디언즈</div>
                            vs
                            <div id="team2">${gameInfo.oppTeamName}</div>
                        </div>
                        <div class="detail-wrapper  ">
                            <div id="location">스타라이트 필드</div>
                            |
                            <div id="date"><fmt:formatDate value="${gameInfo.gameDate}" pattern="yyyy-MM-dd(E) HH:mm" /></div>
                        </div>
                    </div>
                    <div class="mapinfo-container">
                        <div class="mapinfo-wrapper">
                            <%@ include file ="toolbar.jsp" %>
                            <div class="selectedList-container">
                                <div class="selectedList-info-container">
                                    <div class="selectedList-dropdown-wrapper">
                                        <img id="selectedList-dropdown" src="/assets/img/reservation/dropDownWhiteUp.svg" alt="드롭다운">
                                    </div>
                                    <div class="selectedList-infoText-wrapper">
                                        <div id="selectedList-infoText">
                                            선택된 좌석 (<span id="selectList-num" style="display: inline">0</span>석)
                                        </div>
                                    </div>
                                    <div class="resetBtn-wrapper">
                                        <img id="resetBtn" src="/assets/img/reservation/resetBtn.svg" alt="초기화 버튼">
                                    </div>
                                </div>
                                <div   class="selectedList-comp-container">
                                    <div class="selectedList-comp-wrapper">

                                    </div>
                                </div>
                            </div>
                            <%@ include file = "stadium.jsp" %>
                            <%@ include file = "seats.jsp" %>
                        </div>
                    </div>
                </div>
            </div>
            <div class="right-container">
                <div class="right-wrapper">
                    <div class="top-container">
                        <div class="top-infotext-container">
                            <div class="top-infotext-wrapper">
                                <div id="infotext"></div>
                            </div>
                        </div>
                        <div class="top-infoImg-container">
                            <div class="top-infoImg-wrapper">
                                <img id="infoImg" src="${pageContext.request.contextPath}/assets/img/reservation/ballparkView.svg" alt="전체보기">
                            </div>
                        </div>
                    </div>
                    <div class="bottom-container">
                        <div class="bottom-infotext-container">
                            <div class="selectZone-wrapper">
                                <div id="selectZone">등급 선택</div>
                            </div>
                            <div id="zoneReloadBtn-wrapper">
                                <img id="zoneReloadBtn" src="${pageContext.request.contextPath}/assets/img/reservation/reloadBtnSmall.svg" alt="새로고침 버튼">
                            </div>
                        </div>
                        <div class="zoneInfo-container">
                            <div class="zoneInfo-wrapper">
                                <div class="zoneInfo-listBox">
                                    <!-- 구역 -->
                                    <c:forEach var="zone" items="${zoneMap}">
                                        <div class="zoneInfo" id="${zone.zonePk}zone">
                                            <div class="zoneColor-wrapper">
                                                <div id="zoneColor" style="background-color: ${zone.zoneColor}"></div>
                                            </div>
                                            <div class="zoneName-wrapper">
                                                <div id="zoneName">
                                                        ${zone.zoneName}
                                                </div>
                                            </div>
                                            <div class="zoneVacancies-wrapper">
                                                <div id="zoneVacancies">
                                                        ${zone.remainingNum}석
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ticket-btn-container">
                    <div class="ticket-btn-wrapper">
                        <img id="ticket-btn" src="${pageContext.request.contextPath}/assets/img/reservation/nextBtn.svg" alt="다음버튼">
                    </div>
                    <div class="autoAssigned">
                        <%@include file="autoAssigned.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="loader">Loading...</div>
<div class="loading-overlay"></div>
<div class="overlay"></div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/reservation/tickets1.js"></script>
</body>
</html>
