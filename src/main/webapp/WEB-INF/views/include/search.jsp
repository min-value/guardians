<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/assets/css/include/search.css">
<%-- ================ < search bar >================ --%>
<form class="search" action="" method="get">
  <select class="dropdown" name="type">
    <option value="all" selected>전체</option>
    <option value="${content1Value}">${content1Text}</option>
    <option value="${content2Value}">${content2Text}</option>
  </select>
  <input class="search-text" type="text" placeholder="검색어를 입력하세요.">
  <button class="search-btn" type="submit" onclick="alert('클릭!')"></button>
</form>
<%-- ============================================ --%>