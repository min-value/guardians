<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/fairy/fairy.css">
</head>
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<body>
  <%@ include file="../include/header.jsp" %>
  <div class="container">
      <div class="title">
          <div class="subtitle">
              우리를 승리로 이끈 힘,
          </div>
          <div class="maintitle">
              이번 시즌의 승리 요정들!
          </div>
      </div>
      <div class="content">
          <div class="fairy-container">
              <div class="box-container">
                  <c:forEach var="dto" items="${list}">
                      <div class="box">
                          <img src="/assets/img/fairy/${dto.place}.png" alt="gold" class="medal" width="90px">
                          <div class="name">${dto.userName}</div>

                          <table>
                              <tr>
                                  <td>직관</td>
                                  <td style="padding: 0 20px;">|</td>
                                  <td>${dto.totalCnt} 회</td>
                                  <td>
                                      <div class="bar-container">
                                          <div class="bar" style="width: ${dto.totalCnt * 40}px;"></div>
                                      </div>
                                  </td>
                              </tr>
                              <tr>
                                  <td>승리</td>
                                  <td style="padding: 0 20px;">|</td>
                                  <td>${dto.winCnt} 회</td>
                                  <td>
                                      <div class="bar-container">
                                          <div class="bar" style="width: ${dto.winCnt * 40}px; background-color: #22C55E;"></div>
                                      </div>
                                  </td>
                              </tr>
                              <tr>
                                  <td>무승부</td>
                                  <td style="padding: 0 20px;">|</td>
                                  <td>${dto.drawCnt} 회</td>
                                  <td>
                                      <div class="bar-container">
                                          <div class="bar" style="width: ${dto.drawCnt * 40}px; background-color: #FACC15;"></div>
                                      </div>
                                  </td>
                              </tr>
                              <tr>
                                  <td>패배</td>
                                  <td style="padding: 0 20px;">|</td>
                                  <td>${dto.loseCnt} 회</td>
                                  <td>
                                      <div class="bar-container">
                                          <div class="bar" style="width: ${dto.loseCnt * 40}px; background-color: #EF4444;"></div>
                                      </div>
                                  </td>
                              </tr>
                              <tr>
                                  <td>승률</td>
                                  <td style="padding: 0 20px;">|</td>
                                  <td>${dto.ratio}</td>
                                  <td>
                                      <div class="bar-container">
                                          <div class="bar" style="width: ${dto.ratio * 300}px; background-color: #0EA5E9;"></div>
                                      </div>
                                  </td>
                              </tr>
                          </table>
                      </div>
                  </c:forEach>
              </div>
              <div class="fairy-img">
                  <img src="/assets/img/fairy/fairyMove.webp" width="850px" alt="fairy">
              </div>
          </div>
      </div>
  </div>
  <%@ include file="../include/footer.jsp"%>
</body>
</html>
