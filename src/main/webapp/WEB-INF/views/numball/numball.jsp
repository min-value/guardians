<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String pageTitle = "숫자야구";
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/numball/numball.css">
</head>
<body>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<%@ include file="../include/headerImg.jsp" %>

<div class="game-wrapper">
    <div class="game-container">
        <div class="game-left">
            <div class="input-group">
                <input type="text" maxlength="1" class="numbox" id="num0_1" disabled>
                <input type="text" maxlength="1" class="numbox" id="num0_2" disabled>
                <input type="text" maxlength="1" class="numbox" id="num0_3" disabled>
                <div class="result-wrapper" id="resultBox0">
                    <button class="check-btn" id="checkBtn0" disabled>확인</button>
                    <div class="result-box" style="display: none;"></div>
                </div>
            </div>
            <div class="input-group">
                <input type="text" maxlength="1" class="numbox" id="num1_1" disabled>
                <input type="text" maxlength="1" class="numbox" id="num1_2" disabled>
                <input type="text" maxlength="1" class="numbox" id="num1_3" disabled>
                <div class="result-wrapper" id="resultBox1">
                    <button class="check-btn" id="checkBtn1" disabled>확인</button>
                    <div class="result-box" style="display: none;"></div>
                </div>
            </div>
            <div class="input-group">
                <input type="text" maxlength="1" class="numbox" id="num2_1" disabled>
                <input type="text" maxlength="1" class="numbox" id="num2_2" disabled>
                <input type="text" maxlength="1" class="numbox" id="num2_3" disabled>
                <div class="result-wrapper" id="resultBox2">
                    <button class="check-btn" id="checkBtn2" disabled>확인</button>
                    <div class="result-box" style="display: none;"></div>
                </div>
            </div>
            <div class="input-group">
                <input type="text" maxlength="1" class="numbox" id="num3_1" disabled>
                <input type="text" maxlength="1" class="numbox" id="num3_2" disabled>
                <input type="text" maxlength="1" class="numbox" id="num3_3" disabled>
                <div class="result-wrapper" id="resultBox3">
                    <button class="check-btn" id="checkBtn3" disabled>확인</button>
                    <div class="result-box" style="display: none;"></div>
                </div>
            </div>
            <div class="input-group">
                <input type="text" maxlength="1" class="numbox" id="num4_1" disabled>
                <input type="text" maxlength="1" class="numbox" id="num4_2" disabled>
                <input type="text" maxlength="1" class="numbox" id="num4_3" disabled>
                <div class="result-wrapper" id="resultBox4">
                    <button class="check-btn" id="checkBtn4" disabled>확인</button>
                    <div class="result-box" style="display: none;"></div>
                </div>
            </div>
            <div class="input-group">
                <input type="text" maxlength="1" class="numbox" id="num5_1" disabled>
                <input type="text" maxlength="1" class="numbox" id="num5_2" disabled>
                <input type="text" maxlength="1" class="numbox" id="num5_3" disabled>
                <div class="result-wrapper" id="resultBox5">
                    <button class="check-btn" id="checkBtn5" disabled>확인</button>
                    <div class="result-box" style="display: none;"></div>
                </div>
            </div>
        </div>
        <div class="game-right">
            <div class="img-container">
                <img id="openGuideBtn"
                     src="${pageContext.request.contextPath}/assets/img/icon/interrogation.svg"
                     alt="게임 설명">
                <img id="openPointBtn"
                     src="${pageContext.request.contextPath}/assets/img/icon/point.svg"
                     alt="포인트 설명">
                <img src="${pageContext.request.contextPath}/assets/img/mypage/numball.png"
                     alt="야구 캐릭터"
                     class="numball-image">
            </div>
        </div>
    </div>

    <div id="guideModal" class="modal" style="display: none;">
        <div class="modal-content">
            <h2 class="modal-title">숫자야구 어떻게 하나요?</h2>
            <p class="modal-description">
                세 자리 숫자를<br>
                6번의 도전 안에 맞혀보세요!
            </p>
            <p class="modal-description">
                숫자와 자리가 모두 맞으면 <span class="highlight">스트라이크(S)</span><br>
                숫자만 맞고 자리는 다르면 <span class="highlight">볼(B)</span><br>
                숫자가 하나도 맞지 않으면 <span class="highlight">아웃(OUT)</span>입니다.
            </p>
            <p class="modal-description">
                숫자는 0~9까지 구성되어 있으며,<br>
                각 숫자는 한번씩만 사용 가능합니다.
            </p>
            <p class="modal-description">
                *숫자야구는 매일 자정에 갱신됩니다.<br>
                *사용자마다 랜덤하게 다른 숫자를 지정받습니다.
            </p>
            <button type="button" id="closeGuide" class="close-button"></button>
            <button id="confirmGuide" class="confirm-button" type="button">확인</button>
        </div>
    </div>

    <div id="pointModal" class="modal" style="display: none;">
        <div class="modal-content">
            <h2 class="modal-title">포인트 적립 안내</h2>
            <p class="modal-description">
                숫자야구에 참여하면<br>
                <span class="highlight">매일 포인트를 적립</span>할 수 있어요!
            </p>
            <p class="modal-description">
                첫 번째 시도에 성공 시 <span class="highlight">50P</span><br>
                두 번째 시도에 성공 시 <span class="highlight">30P</span><br>
                세 번째 시도에 성공 시 <span class="highlight">20P</span><br>
                네 번째 시도에 성공 시 <span class="highlight">10P</span><br>
                다섯 번째 시도에 성공 시 <span class="highlight">5P</span><br>
                마지막 시도에 성공 시 <span class="highlight">3P</span><br>
            </p>
            <p class="modal-description">
                *하루 한 번 참여 가능하며,<br>
                포인트는 게임 종료 후 자동 적립됩니다.
            </p>
            <button type="button" id="closePoint" class="close-button"></button>
            <button id="confirmPoint" class="confirm-button" type="button">확인</button>
        </div>
    </div>


</div>

<%@ include file="/WEB-INF/views/include/footer.jsp" %>
<script src="${pageContext.request.contextPath}/assets/js/numball/numball.js"></script>
<script>
    const openBtn = document.getElementById('openGuideBtn');
    const modal = document.getElementById('guideModal');
    const closeBtn = document.getElementById('closeGuide');
    const confirmBtn = document.getElementById('confirmGuide');

    const pointModal = document.getElementById('pointModal');
    const openPointBtn = document.getElementById('openPointBtn');
    const closePoint = document.getElementById('closePoint');
    const confirmPoint = document.getElementById('confirmPoint');

    openBtn.addEventListener('click', () => {
        modal.style.display = 'flex';
    });

    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    confirmBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });

    openPointBtn.addEventListener('click', () => {
        pointModal.style.display = 'flex';
    });

    closePoint.addEventListener('click', () => {
        pointModal.style.display = 'none';
    });

    confirmPoint.addEventListener('click', () => {
        pointModal.style.display = 'none';
    });
</script>

</body>
</html>
