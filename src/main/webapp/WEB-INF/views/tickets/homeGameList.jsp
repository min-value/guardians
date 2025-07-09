<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String pageTitle = "티켓";
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/homegames.css">
</head>
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
<script>
    const gamePk = Number(${gamePk});

    const user = {
        userPk: "${sessionScope.loginUser.userPk}",
        userName: "${sessionScope.loginUser.userName}",
        tel: "${sessionScope.loginUser.tel}",
        email: "${sessionScope.loginUser.email}",
        totalPoint: "${sessionScope.loginUser.totalPoint}"
    };

    function openSeatReservation(gameNo) {
        const width = 500;
        const height = 400;

        const left = (window.screen.width - width) / 2;
        const top = (window.screen.height - height) / 2;

        // window.open(
        //     '/tickets/queue',
        //     '_blank',
        //     `width=\${width},height=\${height},left=\${left},top=\${top},scrollbars=no,resizable=no`
        // );
        window.open(`/reservation/seat?gamePk=\${gameNo}`, '_blank',
                'width=800,height=700,scrollbars=yes,resizable=no');
    }

    function loadPage(page) {
        const params = new URLSearchParams(window.location.search);
        const teamStatus = params.get('teamStatus');
        const ticketStatus = params.get('ticketStatus');
        const finalTeamStatus = (teamStatus=== null || teamStatus.trim() === '') ? '0' : teamStatus;
        const finalTicketStatus = (ticketStatus=== null || ticketStatus.trim() === '') ? '0' : ticketStatus;

        console.log({ page, teamStatus: finalTeamStatus, ticketStatus: finalTicketStatus });
        console.log("???");
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
                const startDate = new Date(dto.startDate);
                const isOnSale = startDate <= now;

                const buttonHtml = isOnSale
                    ? `<input class="onsale-ticket-btn" type="button" value="예매하기" onclick="redirectIfSessionExists(\${dto.gameNo})">`
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
                                    <img class="teamlogo" src="/assets/img/teamlogos/\${dto.ourTeam}.svg" alt="ourTeam">
                                    vs <img class="teamlogo" src="/assets/img/teamlogos/\${dto.opponentTeam}.svg" alt="opponentTeam">
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
    function redirectIfSessionExists(gamePk) {
        window.open(`/reservation/seat?gamePk=` + gamePk, '_blank', 'width=800,height=700,scrollbars=yes,resizable=no');

        /*
        //권종/할인 선택
        let discountInfo = JSON.parse(localStorage.getItem('discountInfo' + gamePk));
        let gameInfo = JSON.parse(localStorage.getItem('gameInfo' + gamePk));
        let reservelistPk = Number(localStorage.getItem('reservelistPk' + gamePk));
        let seats = JSON.parse(localStorage.getItem('seats' + gamePk));
        let zone = JSON.parse(localStorage.getItem('zone' + gamePk));

        //예매 확인
        let discountPk = JSON.parse(localStorage.getItem('discountPk' + gamePk));
        let totalPay = Number(localStorage.getItem('totalPay' + gamePk));

        if(discountInfo !== null && gameInfo !== null && reservelistPk !== null && seats !== null && zone !== null) {
            if(gamePk === gameInfo['gamePk']) {
                //세션에 존재 -> redis확인
                const sendConfirm = {
                    gamePk: gameInfo['gamePk'],
                    seats: seats,
                    zonePk: zone['zonePk']
                }

                fetch(`/reservation/preemption/confirm`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(sendConfirm)
                })
                    .then(res => {
                        if(res.status === 401) {
                            const redirectUrl = res.headers.get("Location") || "/reservation/errors/needLogin";
                            window.location.href = redirectUrl;
                            return;
                        } else {
                            return res.json();
                        }
                    })
                    .then(data => {
                        if (data === 2) {
                            localStorage.clear();
                        } else if (data === 0) {
                            localStorage.clear();
                        } else {
                            //선점이 되어있으면
                            if (discountPk !== null && totalPay !== null && !isNaN(totalPay)) {
                                if(confirm(`이전 예매 기록이 있습니다. 불러오시겠습니까?`)) {
                                    localStorage.removeItem('discountPk');
                                    localStorage.removeItem('totalPay');

                                    window.open(`/reservation/confirm?gamePk=` + gamePk, '_blank', 'width=800,height=700,scrollbars=yes,resizable=no');
                                } else {
                                    //DB에서 삭제
                                    let sendData = {
                                        gamePk: gamePk,
                                        seats: seats,
                                        zonePk: Number(zone['zonePk']),
                                        reservelistPk: reservelistPk
                                    };

                                    console.log(sendData);
                                    fetch(`/reservation/preemption/delete`, {
                                        method: 'DELETE',
                                        headers: {
                                            'Content-Type': 'application/json'
                                        },
                                        body: JSON.stringify(sendData)
                                    })
                                        .finally(() => {
                                            localStorage.clear();
                                            window.open(`/reservation/seat?gamePk=` + gamePk, '_blank', 'width=800,height=700,scrollbars=yes,resizable=no');
                                        });
                                }
                            } else {
                                if(confirm(`이전 예매 기록이 있습니다. 불러오시겠습니까?`)) {
                                    window.open(`/reservation/discount?gamePk=` + gamePk, '_blank', 'width=800,height=700,scrollbars=yes,resizable=no');
                                } else {
                                    //DB에서 삭제
                                    let sendData = {
                                        gamePk: gamePk,
                                        seats: seats,
                                        zonePk: Number(zone['zonePk']),
                                        reservelistPk: reservelistPk
                                    };
                                    console.log(sendData);
                                    fetch(`/reservation/preemption/delete`, {
                                        method: 'DELETE',
                                        headers: {
                                            'Content-Type': 'application/json'
                                        },
                                        body: JSON.stringify(sendData)
                                    })
                                        .finally(() => {
                                            localStorage.clear();
                                            window.open(`/reservation/seat?gamePk=` + gamePk, '_blank', 'width=800,height=700,scrollbars=yes,resizable=no');
                                        });
                                }
                            }
                        }
                    })
                    .catch(error => {
                        alert(`서버 오류 발생` + error);
                        localStorage.clear();
                    });
            }
        } else {
            localStorage.clear();
            window.open(`/reservation/seat?gamePk=` + gamePk, '_blank', 'width=800,height=700,scrollbars=yes,resizable=no');
        }

         */
    }

  $(document).ready(function () {
      loadPage(1);
      const showModal = "${showModal}" === "true";
      if (showModal) {
          document.querySelector('.modal').style.display = 'flex';
      }
  });
</script>
<body>
  <%@ include file="../include/header.jsp" %>
  <%@ include file="../include/headerImg.jsp" %>
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
  <%@ include file="../include/footer.jsp" %>
</body>
</html>
