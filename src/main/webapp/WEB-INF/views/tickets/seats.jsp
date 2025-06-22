<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/seats.css" />

<div class="seats-container">
    <%@ include file ="toolbar.jsp" %>
    <div class="direction-container">
        <img id="directionInfo" src="${pageContext.request.contextPath}/assets/img/tickets/directionInfo.svg" alt="경기장 방향">
    </div>
    <div class="map-container zoomP">
        <div class="map-wrapper zoomC">
            <object id="svgMap" type="image/svg+xml" data=""></object>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/tickets/seats.js"></script>
