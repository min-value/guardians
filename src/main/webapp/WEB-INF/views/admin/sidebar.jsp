<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
  <link rel="stylesheet" href="/assets/css/colors.css"/>
  <link rel="stylesheet" href="/assets/css/admin/admin.css"/>
</head>
<body>
<%String activePage = (String) request.getAttribute("activePage");%>
<div class="sidebar">
  <div class="admin_main">
    <a href="/admin/home" class="<%= "main".equals(activePage) ? "active" : "" %>"><img src="/assets/img/icon/adminmain.png" alt="icon"/>관리자 메인</a>
  </div>
  <div class="admin_tickets">
    <a href="/admin/tickets/reservation" class="<%= "reservation".equals(activePage) ? "active" : "" %>"><img src="/assets/img/icon/list.png" alt="icon"/>예매 목록 조회</a>
  </div>
  <div class="admin_games">
    <a href="/admin/games/allgame" class="<%= "gamelist".equals(activePage) ? "active" : "" %>"><img src="/assets/img/icon/list.png" alt="icon"/>경기 목록 조회</a>
  </div>
  <div class="admin_add_games">
    <a href="/admin/tickets/addgame" class="<%= "addgames".equals(activePage) ? "active" : "" %>"><img src="/assets/img/icon/schedule.png" alt="icon"/>경기 예매 등록</a>
  </div>
</div>
</body>
</html>
