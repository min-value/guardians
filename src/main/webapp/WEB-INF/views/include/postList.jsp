<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/assets/css/include/postList.css">
<div class="post">
  <div class="title">${post.title}</div>
  <div class="writer">${post.userName}</div>
  <div class="date">${post.p_date}</div>
</div>