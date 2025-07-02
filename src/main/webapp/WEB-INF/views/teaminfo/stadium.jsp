<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/include/header.jsp" />
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/teaminfo/stadium.css">
    <link rel="stylesheet" href="/assets/css/font.css">
</head>
<body>
<div class="header-title">
  <img src="/assets/img/header/header-title.png" alt="헤더">
  <div style="position: absolute; color: black; font-size: 48px; font-weight: 600;">
    경기장 소개
  </div>
</div>

<div class="info-wrapper">
  <img src="/assets/img/teaminfo/stadium.png" class="stadium-img">

    <div class="small-title">
        <div class="stadium-name">스타라이트 필드</div>
        <div class="stadium-detail">Starlight Field</div>
    </div>

    <hr class="line">

    <div class="content-box">
        <div class="content-title">경기장의 상징, Starlight Field</div>
        <div class="content-text">
            스타라이트 필드는 신한 가디언즈의 홈구장으로,
            전통성과 현대성을 조화롭게 결합한 대표적인 야구 경기장입니다.
            약 40,000명을 수용할 수 있는 관람석은 관중의 몰입도를 극대화할 수 있도록 설계되었으며,
            원형 배치와 경사 조절을 통해 어떤 좌석에서도 쾌적한 시야를 확보할 수 있습니다.
            경기장 외관은 강인함을 상징하는 기사단의 갑옷을 모티브로 설계되어,
            구단의 정체성과 상징성을 극대화합니다.
        </div>
    </div>

    <hr class="line">

    <div class="content-box">
        <div class="content-title">밤하늘 아래 빛나는 최첨단 시설</div>
        <div class="content-text">
            스타라이트 필드라는 이름에 걸맞게, 야간 경기를 위한 LED 조명 시스템은 마치 별빛이 내려앉은 듯한 환상적인 분위기를 연출합니다.
            대형 전광판은 고해상도 중계 및 실시간 정보 제공 기능을 탑재하고 있으며,
            선수들의 경기력을 극대화할 수 있는 최신 훈련 시설과 회복 공간도 함께 갖추고 있습니다.
            관중을 위한 스마트 티켓, 무인 키오스크, 모바일 연동 앱 서비스 등 디지털 인프라 역시 완비되어 있습니다.
        </div>
    </div>

    <hr class="line">

    <div class="content-box">
        <div class="content-title">팬과 함께하는 복합 스포츠 문화 공간</div>
        <div class="content-text">
            스타라이트필드는 단순한 스포츠 경기장을 넘어,
            다양한 세대가 함께 즐길 수 있는 복합 문화 공간으로 자리매김하고 있습니다.
            경기장 내부에는 팀 굿즈 숍, 프리미엄 라운지, 지역 기반의 푸드존 등 팬을 위한 맞춤형 시설이 마련되어 있으며,
            경기 외에도 팬미팅, 콘서트, 어린이 프로그램 등 다양한 이벤트가 연중 진행됩니다.
            이는 가디언즈가 단순한 구단을 넘어 하나의 지역 커뮤니티로 확장되고 있다는 증거이기도 합니다.
        </div>
    </div>

</div>


<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>
