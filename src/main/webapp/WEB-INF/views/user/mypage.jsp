<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>마이페이지</title>
    <link rel="stylesheet" href="/assets/css/user/mypage.css">
</head>
<body>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<div class="header-title" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
    <!--나중에 컴포넌트로 바꿀 것.-->
    <img src="/assets/img/header/header-title.png" alt="헤더 타이틀" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
</div>
<div class="mypage-container">
    <div class="mypage-tabs">
        <button class="tab-btn active" data-tab="info">내 정보</button>
        <button class="tab-btn" data-tab="tickets">예매내역</button>
        <button class="tab-btn" data-tab="points">포인트내역</button>
        <button class="tab-btn" data-tab="fairy">승리요정</button>
    </div>

    <div id="info" class="tab-content active">
        <%@ include file="/WEB-INF/views/user/mypage/info.jsp" %>
    </div>

    <div id="tickets" class="tab-content">
        <%@ include file="/WEB-INF/views/user/mypage/tickets.jsp" %>
    </div>

    <div id="points" class="tab-content">
        <%@ include file="/WEB-INF/views/user/mypage/points.jsp" %>
    </div>

    <div id="fairy" class="tab-content">
        <%@ include file="/WEB-INF/views/user/mypage/fairy.jsp" %>
    </div>

</div>

<script>
    const tabs = document.querySelectorAll('.mypage-tabs .tab-btn');
    const contents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            contents.forEach(c => c.classList.remove('active'));
            const target = tab.getAttribute('data-tab');
            document.getElementById(target).classList.add('active');
        });
    });
</script>

</body>
</html>