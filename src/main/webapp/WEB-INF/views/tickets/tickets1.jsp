<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/tickets-common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/tickets1.css">
</head>
<body>
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
                            <div id="team2">삼성 라이온즈</div>
                        </div>
                        <div class="detail-wrapper">
                            <div id="location">스타라이트 필드</div>
                            |
                            <div id="date">2025-06-16(월) 18:30</div>
                        </div>
                    </div>
                    <div class="mapinfo-container">
                        <%@ include file = "stadium.jsp" %>
<%--                        <%@ include file = "seats.jsp" %>--%>
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
                                <img id="infoImg" src="${pageContext.request.contextPath}/assets/img/tickets/ballparkView.svg" alt="전체보기">
                            </div>
                        </div>
                    </div>
                    <div class="bottom-container">
                        <div class="bottom-infotext-container">
                            <div class="selectZone-wrapper">
                                <div id="selectZone">등급 선택</div>
                            </div>
                            <div id="zoneReloadBtn-wrapper">
                                <img id="zoneReloadBtn" src="${pageContext.request.contextPath}/assets/img/tickets/reloadBtnSmall.svg" alt="새로고침 버튼">
                            </div>
                        </div>
                        <div class="zoneInfo-container">
                            <div class="zoneInfo-wrapper">
                                <div class="zoneInfo-listBox">
                                    <div class="zoneInfo">
                                        <div class="zoneColor-wrapper">
                                            <div id="zoneColor"></div>
                                        </div>
                                        <div class="zoneName-wrapper">
                                            <div id="zoneName">
                                                VIP석
                                            </div>
                                        </div>
                                        <div class="zoneVacancies-wrapper">
                                            <div id="zoneVacancies">
                                                0석
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ticket-btn-container">
                    <div class="ticket-btn-wrapper">
                        <img src="${pageContext.request.contextPath}/assets/img/tickets/nextBtn.svg" alt="다음버튼">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/tickets/tickets1.js"></script>
</body>
</html>
