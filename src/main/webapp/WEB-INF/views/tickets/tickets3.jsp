<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/tickets-common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/tickets3.css">
</head>
<body>
<!-- 예매 확인 -->
<div class="full-container">
    <jsp:include page="topbar.jsp" flush="false">
        <jsp:param name="type" value="1"/>
    </jsp:include>
    <div class="mainback-container">
        <div class="main-container">
            <div class="left-container">
                <div class="left-wrapper">

                </div>
            </div>
            <div class="right-container">
                <div class="right-wrapper">
                    <div class="top-container">

                    </div>
                    <div class="bottom-container">

                    </div>
                </div>
                <div class="ticket-btn-container">
                    <img src="${pageContext.request.contextPath}/assets/img/tickets/backBtnSmall.svg" alt="이전버튼">
                    <img src="${pageContext.request.contextPath}/assets/img/tickets/payBtn.svg" alt="다음버튼">
                </div>
            </div>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/tickets/3.js"></script>
</body>
</html>
