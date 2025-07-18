<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/assets/css/include/search.css">
<%-- ================ < search bar >================ --%>
<form id="searchForm" class="search"  method="get">
  <select class="dropdown" name="type">
    <option value="all" selected>전체</option>
    <option value="<%=content1Value%>"><%=content1Text%></option>
    <option value="<%=content2Value%>"><%=content2Text%></option>
  </select>
  <input class="search-text" type="text" placeholder="검색어를 입력하세요." name="keyword">
  <button id="searchBtn" class="search-btn" type="submit"></button>
</form>
<%-- ============================================ --%>

<script>
  document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("searchForm");
    const button = document.getElementById("searchBtn");
    const keywordInput = form.querySelector("input[name = 'keyword']");

    button.addEventListener("click", () => {
      const keyword = keywordInput.value.trim();
      if(keyword === ''){
          alert("검색어를 입력해주세요!");
      }
      else form.submit();
    });
  });
</script>