<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/include/header.jsp" />
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/teaminfo/club.css">
    <link rel="stylesheet" href="/assets/css/font.css">
</head>
<body>
<div class="header-title">
    <img src="/assets/img/header/header-title.png" alt="헤더">
    <div style="position: absolute; color: black; font-size: 48px; font-weight: 600;">
        구단 소개
    </div>
</div>

<div class="club-wrapper">
    <div class="team-info-header">
        <img src="/assets/img/teaminfo/team-info-header.png" alt="구단소개">
    </div>

    <div class="title-text">
        <div class="first-text">
            <span class="title-text-1">BASEBALL CLUB</span>
            <span class="title-text-2"> SHINHAN가디언즈</span>
        </div>
        <div class="second-text">
            <span class="title-text-3">우리는 지킨다, 승리를. 그리고 가치를.</span>
        </div>
        <br>
    </div>

    <div class="body-wrapper">
        <hr class="line">

        <div class="content-box">
            <img src="/assets/img/teaminfo/club-image-1.png" class="img">
            <div class="text-box">
                <div class="semi-title">처음부터 끝까지, 정면승부</div>
                <div class="content">
                    SHINHAN 가디언즈는 결과보다 과정을 중시합니다. <br>
                    한 타석, 한 이닝, 한 경기를 소중히 여기며, 늘 진심으로 승부에 임합니다. <br>
                    치열한 훈련과 전략, 그리고 포기하지 않는 마음. <br>
                    우리는 모든 경기에서 가디언즈다운 모습을 보여줍니다.
                </div>
            </div>
        </div>

        <hr class="line">

        <div class="content-box">
            <img src="/assets/img/teaminfo/club-image-2.png" class="img">
            <div class="text-box">
                <div class="semi-title">팀워크로 하나 된 '가디언즈' 정신</div>
                <div class="content">
                    선수, 코치진, 프런트가 함께 만들어가는 진짜 야구. <br>
                    SHINHAN 가디언즈의 저력은 서로를 믿는 힘에서 나옵니다. <br>
                    그라운드 안팎을 가리지 않고, 모두가 같은 목표를 향해 움직입니다. <br>
                    그게 바로 '가디언즈'라는 이름이 가진 진짜 의미입니다.
                </div>
            </div>
        </div>

        <hr class="line">

        <div class="content-box">
            <img src="/assets/img/teaminfo/club-image-3.png" class="img">
            <div class="text-box">
                <div class="semi-title">미래를 밝히는 이름들</div>
                <div class="content">
                    SHINHAN 가디언즈는 유망주의 성장과 스타의 탄생이 끊이지 않습니다. <br>
                    경기를 뒤집는 한 방, 완벽한 수비, 흔들림 없는 투구까지. <br>
                    SHINHNA 가디언즈는 야구의 감동을 만드는 선수들로 팬들과 만납니다. <br>
                </div>
            </div>
        </div>

        <hr class="line">

        <div class="last-text-box">
            <div class="last-text">우리는 야구를 통해 지켜야 할 가치를 압니다.</div>
            <div class="last-text">그리고 그것을 지키기 위해, 언제나 앞으로 나아갑니다.</div>
            <img src="/assets/img/teaminfo/catch_praise.png" class="catch-praise">
        </div>
    </div>
</div>


<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>
