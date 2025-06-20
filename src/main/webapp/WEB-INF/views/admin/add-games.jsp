<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script>
        function openModal(gameNo){
            fetch('/admin/tickets/gameinfo?gameNo=' + gameNo)
                .then(response => response.json())
                .then(data => {
                    // 모달 내부에 값 삽입
                    document.querySelector('#modal-opponentTeam').value = data.opponentTeamName;
                    document.querySelector('#modal-gameDate').value = data.gameDate;
                    document.querySelector('#modal-stadium').value = data.stadium;

                    // 모달 열기
                    document.querySelector('.modal').style.display = 'flex';
                });

        }
    </script>
</head>
<body>
<%request.setAttribute("activePage", "addgames");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="container">
    <div class="title">
        <img src="/assets/img/icon/schedule.png" alt="icon"/>
        <p>경기 예매 등록</p>
    </div>
    <div class="content">
        <table>
            <thead>
                <tr>
                    <th>No</th>
                    <th>경기정보</th>
                    <th>스코어</th>
                    <th>결과</th>
                    <th>경기날짜</th>
                    <th>예매등록</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="dto" items="${list}">
                <tr>
                    <td>${dto.no}</td>
                    <td><img class="teamlogo" src="/assets/img/teamlogos/${dto.ourTeam}.png" alt="ourTeam">
                        vs <img class="teamlogo" src="/assets/img/teamlogos/${dto.opponentTeam}.png" alt="opponentTeam"></td>
                    <td>${dto.ourScore}:${dto.opponentScore}</td>
                    <td>${dto.result}</td>
                    <td>${dto.gameDate}</td>
                    <td>
                        <input class="save_btn" type="button" value="예매등록" onclick="openModal(${dto.gameNo})">
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="modal">
        <div class="modal-body">
            <h3>예매 등록</h3>
            <div class="form-row">
                <label for="modal-opponentTeam">원정팀:</label>
                <input type="text" id="modal-opponentTeam" readonly>
            </div>
            <div class="form-row">
                <label for="modal-gameDate">경기날짜:</label>
                <input type="text" id="modal-gameDate" readonly>
            </div>
            <div class="form-row">
                <label for="modal-stadium">경기장:</label>
                <input type="text" id="modal-stadium" readonly>
            </div>
            <div class="form-row">
                <label for="startDate">예매 시작일:</label>
                <input type="datetime-local" name="startDate" id="startDate" value="날짜를 선택해주세요.">
            </div>
            <div class="form-row">
                <label for="endDate">예매 마감일:</label>
                <input type="datetime-local" name="endDate" id="endDate" value="날짜를 선택해주세요.">
            </div>
            <div class="btn_container">
                <input type="submit" value="취소" onclick="document.querySelector('.modal').style.display='none'" class="close_btn">
                <input type="submit" value="저장" class="save_btn">
            </div>
        </div>
    </div>
</div>
</body>
</html>
