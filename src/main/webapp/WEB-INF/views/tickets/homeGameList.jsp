<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String pageTitle = "티켓";
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/homegames.css">
    <script type="module" src="${pageContext.request.contextPath}/assets/js/tickets/predict.js"></script>
</head>
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
<script>

    function loadPage(page) {
        const params = new URLSearchParams(window.location.search);
        const teamStatus = params.get('teamStatus');
        const ticketStatus = params.get('ticketStatus');
        const finalTeamStatus = (teamStatus=== null || teamStatus.trim() === '') ? '0' : teamStatus;
        const finalTicketStatus = (ticketStatus=== null || ticketStatus.trim() === '') ? '0' : ticketStatus;

        console.log({ page, teamStatus: finalTeamStatus, ticketStatus: finalTicketStatus });

        $.ajax({
          url: '/tickets/allgames',
          method: 'GET',
          data: {
            page: page,
            teamStatus : finalTeamStatus,
            ticketStatus : finalTicketStatus,
          },
          success: function(res) {
            const list = res.list;
            const totalCount = res.totalCount;
            console.log(res);
            const container = $('#list').empty();

              if (list.length === 0) {
                  container.append(`
                    <div class="no-result">
                        예정된 경기가 없습니다.
                    </div>
                `);
                  return;
              }
            list.forEach(dto => {
                const now = new Date();
                console.log(now);
                const startDate = new Date(dto.startDate);
                const isOnSale = startDate <= now;
                console.log(startDate);
                console.log(isOnSale);

                const buttonHtml = isOnSale
                    ? `<input class="onsale-ticket-btn" type="button" value="예매하기" onclick="window.open('/reservation/seat?gamePk=\${dto.gameNo}', '_blank', 'width=800,height=700,scrollbars=yes,resizable=no');">`
                    : `
                        <div class="plan-ticket-btn">
                          <span class="plan-time">\${dto.date} \${dto.time}</span>
                          <span class="plan-label">판매 예정</span>
                        </div>
                      `;
              container.append(`
                                <div class="box">
                                  <div class=date-container>
                                    <p class="date">\${dto.date}</p>
                                    <div class="day-container">
                                      <p class="day">\${dto.day}</p>
                                      <p class="time">\${dto.time}</p>
                                    </div>
                                  </div>
                                  <div class="team-container">
                                    <img class="teamlogo" src="/assets/img/teamlogos/\${dto.ourTeam}.png" alt="ourTeam">
                                    vs <img class="teamlogo" src="/assets/img/teamlogos/\${dto.opponentTeam}.png" alt="opponentTeam">
                                  </div>
                                  <div>
                                    <p class="stadium">구장</p>
<!--                                    <p class="location">\${dto.stadium}</p>-->
                                        <p class="location">스타라이트 필드</p>
                                  </div>
                                  \${buttonHtml}
                                </div>
                              `);
            });

            createPagination({
              currentPage: page,
              totalCount: totalCount,
              onPageChange: (newPage) => loadPage(newPage),
              pageSize: 10,
              containerId: '#pagination'
            });
          },
          error: function(err) {
            alert("데이터를 불러오지 못했습니다.");
            console.error(err);
          }
        });
  }

  $(document).ready(function () {
    loadPage(1);
  });
</script>
<body>
  <%@ include file="../include/header.jsp" %>
  <div class="backgroundWrapper">
    <%@ include file="../include/headerImg.jsp" %>
  </div>
  <%@ include file="../tickets/predict.jsp" %>
  <div class="container">
      <div class="filter-container">
        <form method="get" action="${pageContext.request.contextPath}/tickets/all">
          <div class="filter">
              <label>
                <select name="teamStatus" onchange="this.form.submit()">
                  <option value="0" <c:if test="${selectedTeamStatus == '0' || empty selectedTeamStatus}">selected</c:if>>전체 팀</option>
                  <option value="1" <c:if test="${selectedTeamStatus == '1'}">selected</c:if>>한화</option>
                  <option value="2" <c:if test="${selectedTeamStatus == '2'}">selected</c:if>>LG</option>
                  <option value="3" <c:if test="${selectedTeamStatus == '3'}">selected</c:if>>롯데</option>
                  <option value="4" <c:if test="${selectedTeamStatus == '4'}">selected</c:if>>KIA</option>
                  <option value="5" <c:if test="${selectedTeamStatus == '5'}">selected</c:if>>삼성</option>
                  <option value="7" <c:if test="${selectedTeamStatus == '7'}">selected</c:if>>KT</option>
                  <option value="8" <c:if test="${selectedTeamStatus == '8'}">selected</c:if>>NC</option>
                  <option value="9" <c:if test="${selectedTeamStatus == '9'}">selected</c:if>>두산</option>
                  <option value="10" <c:if test="${selectedTeamStatus == '10'}">selected</c:if>>키움</option>
                </select>
              </label>
          </div>
          <div class="filter">
              <label>
                <select name="ticketStatus" onchange="this.form.submit()">
                  <option value="0" <c:if test="${selectedTicketStatus == '0' || empty selectedTicketStatus}">selected</c:if>>전체 예매</option>
                  <option value="1" <c:if test="${selectedTicketStatus == '1'}">selected</c:if>>예매 중</option>
                  <option value="2" <c:if test="${selectedTicketStatus == '2'}">selected</c:if>>판매 예정</option>
                </select>
              </label>
          </div>
        </form>
      </div>
    <div id="list">
    </div>
  </div>
  <div id="pagination"></div>
</body>
</html>
