<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets-common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets-common2.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets2.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
</head>
<body>
<!-- 권종/할인 선택 -->
<div class="full-container">
    <jsp:include page="topbar.jsp" flush="false">
        <jsp:param name="type" value="2"/>
    </jsp:include>
    <div class="mainback-container">
        <div class="main-container">
            <div class="left-container">
                <div class="left-wrapper">
                    <div class="reservation-warning-container">
                        <div class="reservation-warning-wrapper">
                            <div id="reservation-warning-text">선택하신 좌석이 선점되었습니다. 10분 이내 결제하지 않으실 경우 선점된 좌석이 해제됩니다.</div>
                        </div>
                    </div>
                    <div class="ticket-selection-container">
                        <div class="ticket-selection-wrapper">
                            <div class="selection-infotext-container">
                                <div id="selection-infotext">티켓 종류 선택</div>
                            </div>
                            <div class="ticket-options-container">
                                <div class="ticket-options-wrapper">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="right-container">
                <%@ include file="/WEB-INF/views/reservation/reserveInfo.jsp"%>
                <div class="ticket-btn-container">
                    <div class="ticket-btn-wrapper">
                        <img id="backBtn" src="${pageContext.request.contextPath}/assets/img/reservation/backBtnSmall.svg" alt="이전버튼">
                        <img id="nextBtn" src="${pageContext.request.contextPath}/assets/img/reservation/nextBtnSmall.svg" alt="다음버튼">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/reservation/tickets2.js"></script>
</body>
</html>
