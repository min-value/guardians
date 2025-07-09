<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>신한 가디언즈</title>
  </head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/queueModal.css">
    <script>
      const gamePk = Number(${gamePk});

      const user = {
        userPk: "${sessionScope.loginUser.userPk}",
        userName: "${sessionScope.loginUser.userName}",
        tel: "${sessionScope.loginUser.tel}",
        email: "${sessionScope.loginUser.email}",
        totalPoint: "${sessionScope.loginUser.totalPoint}"
      };
      localStorage.setItem("user", user);

      // 진행 바를 업데이트하는 함수
      function updateProgressBar(position, total) {
        const containerWidth = 300;
        const progressBar = document.getElementById("progressBar");

        // 전체 대기열 중에서 앞에 있을수록 바가 길게
        const percent = ((total - position + 1) / total) * 100;
        const widthPx = (percent / 100) * containerWidth;

        progressBar.style.width = widthPx + "px";
      }

      // 대기 순번과 전체 대기열 크기를 가져와서 UI 갱신
      function updateQueueStatus() {
        fetch(`/queue-position/${gamePk}?userPk=${user.userPk}`)
                .then(res => res.text())
                .then(position => {
                  position = Number(position);
                  document.getElementById("waitingNum").innerText = position;

                  // 전체 대기열 크기(bar길이용)
                  fetch(`/queue-size/${gamePk}`)
                          .then(res => res.text())
                          .then(total => {
                            total = Number(total);
                            updateProgressBar(position, total);
                          });
                });
      }

      // 페이지 로드 시 초기 실행 및 3초마다 갱신
      window.onload = () => {
        updateQueueStatus();
        setInterval(updateQueueStatus, 3000);
      };

      window.addEventListener("beforeunload", function () {
        const url = `/queue/leave/${gamePk}?userPk=${user.userPk}`;
        navigator.sendBeacon(url);
      });
    </script>
  <body>
  <div class="modal-container">
    <div class="modal-body">
      <div class="text1">나의 대기 순서</div>
      <div class="waitingNum">${position}</div>
      <div class="bar-container">
        <div class="bar" id="progressBar" style="width: 0;"></div>
      </div>

      <div class="text4">현재 접속량이 많아 대기 중입니다.<br>잠시만 기다려 주시면 예매 페이지로 접속 됩니다.</div>

      <div class="text3">새로고침 하거나 재접속 하시면 대기 순서가<br>초기화 되어 대기 시간이 더 길어집니다.</div>
    </div>
  </div>
  </body>
</html>
