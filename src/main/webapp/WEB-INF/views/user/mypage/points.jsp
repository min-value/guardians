<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="/assets/css/user/mypage/point.css">
<link rel="stylesheet" href="/assets/css/font.css">

<div class="points-tab">
    <div class="points-tab-top">
        <div class="point-title-box">
            <div class="point-title">HomeRun point</div>
            <div class="point-total">0P</div>
        </div>
        <div class="num-ball-box" onclick="location.href='/point/numball'">
            <div class="num-ball-text">
                <div class="num-ball-title">숫자야구 하러가기</div>
                <div class="num-ball-subtitle">
                    포인트 적립 게임
                    <img src="/assets/img/icon/chevron-right.svg" alt=">" class="chevron-icon">
                </div>
            </div>
            <img src="/assets/img/mypage/numball.png" alt="곰 기본" class="num-ball-image default">
            <img src="/assets/img/mypage/numball_2.png" alt="곰 호버" class="num-ball-image hover">
        </div>
    </div>
    <label>적립내역</label>
    <div class="points-list">
        <div class="point-item"></div>
        <!-- 포인트 내역 리스트 동적 렌더링 -->
    </div>
    <div id="points-pagination"></div>
</div>