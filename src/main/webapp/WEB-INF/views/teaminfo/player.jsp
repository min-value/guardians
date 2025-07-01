<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/include/header.jsp" />
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/teaminfo/player.css">
    <link rel="stylesheet" href="/assets/css/font.css">
</head>
<body>
<div class="header-title">
    <img src="/assets/img/header/header-title.png" alt="헤더">
    <div style="position: absolute; color: black; font-size: 48px; font-weight: 600;">
        선수 정보
    </div>
</div>

<div class="info-wrapper">

    <hr class="line">

    <div class="role">감독</div>
    <div class="player-box">
        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/10.png" id="pic">
            </div>
            <div class="name">이호준</div>
        </div>
    </div>

    <hr class="line">

    <div class="role">내야수</div>
    <div class="player-box">
        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/5.png" id="pic">
            </div>
            <div class="name">박세혁</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/2.png" id="pic">
            </div>
            <div class="name">박민우</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/3.png" id="pic">
            </div>
            <div class="name">서호철</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/4.png" id="pic">
            </div>
            <div class="name">김주원</div>
        </div>
    </div>

    <hr class="line">

    <div class="role">외야수</div>
    <div class="player-box">
        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/6.png" id="pic">
            </div>
            <div class="name">손아섭</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/7.png" id="pic">
            </div>
            <div class="name">박건우</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/8.png" id="pic">
            </div>
            <div class="name">권희동</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/9.png" id="pic">
            </div>
            <div class="name">김휘집</div>
        </div>
    </div>

    <hr class="line">

    <div class="role">투수</div>
    <div class="player-box">

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/1.png" id="pic">
            </div>
            <div class="name">전사민</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/11.png" id="pic">
            </div>
            <div class="name">김동현</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/12.png" id="pic">
            </div>
            <div class="name">김한별</div>
        </div>

        <div class="player-info">
            <div class="player-img">
                <img src="/assets/img/teaminfo/13.png" id="pic">
            </div>
            <div class="name">원종해</div>
        </div>
    </div>

    <hr class="line">

</div>

<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>
