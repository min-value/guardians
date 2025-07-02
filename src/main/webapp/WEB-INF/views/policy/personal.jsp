<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "개인정보 처리방침";
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/policy/service.css">
</head>
<body>
    <%@ include file="../include/header.jsp" %>
    <%@ include file="../include/headerImg.jsp" %>
    <div class="content">
        <h1>개인정보 처리방침</h1>
        <p>SHINHAN GUARDIANS BASEBALL CLUB(이하 ‘신한 가디언즈’)은 회원의 개인정보를 소중히 여기며, 관련 법령에 따라 개인정보를 안전하게 보호하고 관리하기 위해 아래와 같은 방침을 수립하고 준수합니다.</p>

        <h3>1. 수집하는 개인정보 항목</h3>
        <p>사이트는 회원가입, 문의, 예매 및 이벤트 응모 등을 위해 다음과 같은 정보를 수집할 수 있습니다.</p>
        <ul>
            <li>필수정보: 이름, 아이디, 비밀번호, 이메일, 휴대전화번호</li>
            <li>선택정보: 생년월일, 주소, 관심팀, 사진</li>
            <li>서비스 이용기록, 접속 로그, 쿠키, 접속 IP 등</li>
        </ul>

        <h3>2. 개인정보 수집 목적</h3>
        <p>수집한 개인정보는 다음의 목적을 위해 활용됩니다.</p>
        <ul>
            <li>회원관리: 본인확인, 가입의사 확인, 불만처리</li>
            <li>서비스 제공: 경기 정보 제공, 티켓 예매, 커뮤니티 운영</li>
            <li>마케팅 및 홍보: 이벤트 안내, 만족도 조사</li>
        </ul>

        <h3>3. 개인정보 보유 및 이용기간</h3>
        <p>회원 탈퇴 시까지 개인정보를 보관하며, 탈퇴 요청 또는 수집 목적 달성 시 지체 없이 파기합니다. 단, 관련 법령에 따라 일정 기간 보존이 필요한 정보는 해당 기간 동안 보존합니다.</p>

        <h3>4. 개인정보 제3자 제공</h3>
        <p>회원의 개인정보는 원칙적으로 외부에 제공하지 않으며, 아래의 경우에만 예외로 제공됩니다.</p>
        <ul>
            <li>회원의 사전 동의를 받은 경우</li>
            <li>법령에 의해 요구되는 경우</li>
        </ul>

        <h3>5. 개인정보의 안전성 확보 조치</h3>
        <p>신한 가디언즈는 개인정보 보호를 위해 다음과 같은 조치를 취하고 있습니다.</p>
        <ul>
            <li>개인정보 암호화 저장 및 관리</li>
            <li>접근권한 최소화 및 정기적 점검</li>
            <li>보안 프로그램 설치 및 주기적인 업데이트</li>
        </ul>

        <h3>6. 이용자의 권리</h3>
        <p>회원은 언제든지 자신의 개인정보를 조회하거나 수정할 수 있으며, 삭제를 요청할 수 있습니다. 개인정보와 관련한 문의는 고객센터 또는 관리자 이메일로 연락해 주세요.</p>

        <h3>7. 개인정보 보호책임자</h3>
        <p>개인정보 보호 관련 문의는 아래로 연락주시면 신속하게 답변드리겠습니다.</p>
        <ul>
            <li class="subin">책임자: 오수빈</li>
            <li>이메일: bread0930@gmail.com</li>
        </ul>
    </div>
    <%@ include file="../include/footer.jsp" %>
</body>
</html>
