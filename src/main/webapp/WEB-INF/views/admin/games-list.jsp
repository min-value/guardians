<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/assets/css/colors.css"/>
    <link rel="stylesheet" href="/assets/css/admin/admin.css"/>
</head>
<body>
<%request.setAttribute("activePage", "gamelist");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="container">
    <div class="title_container">
        <div class="title">
            <img src="/assets/img/icon/list.png" alt="icon"/>
            <p>경기 목록 조회</p>
        </div>
        <div class="logout">
            <a>로그아웃</a>
            <img src="/assets/img/icon/logout.png" alt="icon"/>
        </div>
    </div>
    <div class="content">
        <div class="filter">
            <form method="get" action="/admin/games/allgame">
                <select name="status" onchange="this.form.submit()">
                    <option value="0" <c:if test="${selectedStatus == '0' || empty selectedStatus}">selected</c:if>>전체</option>
                    <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>경기 종료</option>
                    <option value="2" <c:if test="${selectedStatus == '2'}">selected</c:if>>경기 예정</option>
                </select>
            </form>
        </div>
        <table class="filter_table">
            <thead>
                <tr>
                    <th>No</th>
                    <th>경기정보</th>
                    <th>스코어</th>
                    <th>결과</th>
                    <th>경기날짜</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="dto" items="${list}">
                    <c:if test="${selectedStatus == '0' || empty selectedStatus ||
                     (selectedStatus == '1' && dto.status == '1') ||
                     (selectedStatus == '2' && dto.status == '2')}">
                        <tr>
                            <td>${dto.no}</td>
                            <td><img class="teamlogo" src="/assets/img/teamlogos/${dto.ourTeam}.png" alt="ourTeam">
                                 vs <img class="teamlogo" src="/assets/img/teamlogos/${dto.opponentTeam}.png" alt="opponentTeam"></td>
                            <td>${dto.ourScore}:${dto.opponentScore}</td>
                            <td>${dto.result}</td>
                            <td>${dto.gameDate}</td>
                        </tr>
                    </c:if>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
