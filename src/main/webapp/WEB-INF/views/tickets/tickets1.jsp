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
<%--                        <%@ include file = "stadium.jsp" %>--%>
                        <%@ include file = "seats.jsp" %>
                    </div>
                </div>
            </div>
            <div class="right-container">
                <div class="right-wrapper">
                    <div class="top-container">

                    </div>
                    <div class="bottom-container">
                        <div class="infotext-container">
                            <div id="selectZone">등급 선택</div>
                            <div id="zoneReloadBtn-wrapper">
                                <img id="zoneReloadBtn" src="${pageContext.request.contextPath}/assets/img/tickets/reloadBtnSmall.svg" alt="새로고침 버튼">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ticket-btn-container">
                    <img src="${pageContext.request.contextPath}/assets/img/tickets/nextBtn.svg" alt="다음버튼">
                </div>
            </div>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/tickets/tickets1.js"></script>
</body>
</html>
