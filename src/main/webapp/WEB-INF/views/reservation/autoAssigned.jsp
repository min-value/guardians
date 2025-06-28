<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/autoAssigned.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css"> <!-- 제거 필요 -->
<div class="autoAssigned-container">
    <div class="autoAssigned-infoText-container">
        <div class="autoAssigned-infoText">
            좌석 자동배정
        </div>
        <div class="autoAssigned-closeBtn">
            <img src="${pageContext.request.contextPath}/assets/img/reservation/closeBtn2.svg" alt="닫기버튼">
        </div>
    </div>
    <div class="autoAssigned-choice-container">
        <div class="autoAssigned-choiceInfo-wrapper">
            <div id="autoAssigned-choiceInfo">

            </div>
            <div id="remainingSeats">

            </div>
        </div>
        <div class="autoAssigned-choice-wrapper">
            <button class="changeBtn" id="down">-</button>
            <input id="count" type="text" value="0" readonly />
            <button class="changeBtn" id="up">+</button>
        </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/reservation/autoAssigned.js"></script>