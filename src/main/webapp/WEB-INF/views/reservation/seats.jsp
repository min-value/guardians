<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/seats.css" />
<div class="seats-container">
    <div class="direction-container">
        <img id="directionInfo" src="${pageContext.request.contextPath}/assets/img/reservation/directionInfo.svg" alt="경기장 방향">
    </div>
    <div class="map-container zoomP">
        <div class="map-wrapper zoomC">
            <object id="seatObj" type="image/svg+xml" data=""></object>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/reservation/seats.js"></script>