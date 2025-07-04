<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/predict.css">
<script type="module" src="${pageContext.request.contextPath}/assets/js/tickets/predict.js"></script>
<div class="modal">
    <div class="modal-container">
        <div class="modal-header">승부예측</div>
        <div class="modal-body">
            <div class="text1">예매가 성공적으로 완료되었습니다.</div>
            <div class="text2">경기 결과를 맞추고 포인트 받아가세요!</div>
            <div class="team-containers">
                <div class="logo-container">
                    <img class="teamlogos" src="${pageContext.request.contextPath}/assets/img/teamlogos/6.svg" alt="ourTeam" id="ourTeam">
                </div>
                vs
                <div class="logo-container">
                    <img class="teamlogos" src="" alt="opponentTeam" id="opponentTeam">
                </div>
            </div>
            <input class="modal-btn" type="button" value="응원하기" disabled>
            <div class="text3">결과를 맞추면 예매 금액의 8%, 틀리면 2%를 포인트로 드려요</div>
        </div>
    </div>
</div>
