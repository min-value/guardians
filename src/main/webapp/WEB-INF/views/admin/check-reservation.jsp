<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%request.setAttribute("activePage", "reservation");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="container">
    <div class="title">
        <img src="/assets/img/list.png" alt="icon"/>
        <p>예매 목록 조회</p>
    </div>
    <table>
        <thead>
        <tr>
            <th>No</th>
            <th>예매자</th>
            <th>예매번호</th>
            <th>매수</th>
            <th>좌석번호</th>
            <th>상태</th>
            <th>경기날짜</th>
            <th>취소신청일</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="dto" items="${list}">
            <tr>
                <td>${dto.no}</td>
                <td>${dto.name}</td>
                <td>${dto.reserveNo}</td>
                <td>${dto.seatCount}</td>
                <td>
                    <c:forEach var="seat" items="${dto.zoneAndSeat}">
                        ${seat}<br>
                    </c:forEach>
                </td>
                <td>${dto.status}</td>
                <td>${dto.gameDateFormat}</td>
                <td>${dto.cancelDateFormat}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
</body>
</html>
