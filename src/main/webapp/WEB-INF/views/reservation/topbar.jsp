<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String type = request.getParameter("type");
%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/topbar.css">
<div class="topbar-container">
    <div id="topbar1">
        <img id="tkLogo" src="${pageContext.request.contextPath}/assets/img/teamlogos/6.svg" alt="팀 로고">
        <div id="tkText">신한 가디언즈 티켓 예매</div>
    </div>
    <img id="topbar2" src="${pageContext.request.contextPath}/assets/img/reservation/topbar<%=type%>.svg" alt="상단 바">
</div>