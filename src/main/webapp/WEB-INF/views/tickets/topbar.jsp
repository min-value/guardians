<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String type = request.getParameter("type");
%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/topbar.css">
<div class="topbar-container">
    <img id="topbar" src="${pageContext.request.contextPath}/assets/img/tickets/topbar<%=type%>.png" alt="상단 바">
</div>