<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>신한 가디언즈</title>
  </head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/queueModal.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/loading2.css">

  <script>
    function resizeLoading(){
      let loader = document.querySelector('.loader');
      const vw = window.innerWidth;
      const vh = window.innerHeight;

      loader.style.position = 'fixed';
      loader.style.left = (vw / 2) - 50 + 'px';
      loader.style.top = (vh / 2) - 50 + 'px';

    }

      const pathSegments = window.location.pathname.split('/');
      const gamePk = pathSegments[pathSegments.length - 1];

      const user = {
        userPk: "${sessionScope.loginUser.userPk}",
        userName: "${sessionScope.loginUser.userName}",
        tel: "${sessionScope.loginUser.tel}",
        email: "${sessionScope.loginUser.email}",
        totalPoint: "${sessionScope.loginUser.totalPoint}"
      };

      //로딩 띄우기
      function openLoading() {
        let loader = document.querySelector('.loader');
        loader.style.display = 'block'
        resizeLoading();
        document.querySelector('.loading-overlay').style.display = 'flex';
      }

      //로딩 닫기
      function closeLoading() {
        document.querySelector('.loader').style.display = 'none';
        document.querySelector('.loading-overlay').style.display = 'none';
      }

      window.onload = () => {
        window.addEventListener('resize', resizeLoading);
        openLoading();
        // 모달 열리자마자 대기열 등록 요청
        fetch(`/queue/enqueue/\${gamePk}?userPk=\${user.userPk}`, {
          method: 'POST'
        })
                .then(res => {
                  if (!res.ok) throw new Error("대기열 등록 실패");
                  return res.text();
                })
                .then(msg => {
                  closeLoading();
                  updateQueueStatus(); // 대기열 상태 초기화
                  setInterval(updateQueueStatus, 3000); // 이후 3초마다 상태 확인
                })
                .catch(err => {
                  alert("대기열 등록에 실패했습니다. 다시 시도해주세요.");
                  console.error(err);
                });
      };

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
        fetch(`/queue/queue-position/\${gamePk}?userPk=\${user.userPk}`)
                .then(res => res.text())
                .then(position => {
                  position = Number(position) + 1;
                  document.getElementById("waitingNum").innerText = position;

                  // 진행바 계산용 queue size
                  fetch(`/queue/queue-size/\${gamePk}`)
                          .then(res => res.text())
                          .then(total => {
                            total = Number(total);
                            updateProgressBar(position, total);
                          });

                  // 예약 가능 상태 확인
                  fetch(`/queue/can-reserve/\${gamePk}?userPk=\${user.userPk}`)
                          .then(res => res.text())
                          .then(canReserve => {
                            if (canReserve === "true") {
                              location.href = `/reservation/seat?gamePk=` + gamePk;
                            }
                          });
                });
      }

      window.addEventListener("beforeunload", function () {
        const url = `/queue/leave/${gamePk}?userPk=${user.userPk}`;
        navigator.sendBeacon(url);
      });
    </script>
  <body>
  <div class="modal-container">
    <div class="modal-body">
      <div class="text1">나의 대기 순서</div>
      <div class="waitingNum" id="waitingNum">${position}</div>
      <div class="bar-container">
        <div class="bar" id="progressBar" style="width: 0;"></div>
      </div>

      <div class="text4">현재 접속량이 많아 대기 중입니다.<br>잠시만 기다려 주시면 예매 페이지로 접속 됩니다.</div>

      <div class="text3">새로고침 하거나 재접속 하시면 대기 순서가<br>초기화 되어 대기 시간이 더 길어집니다.</div>
    </div>
  </div>
  <div class="loader">Loading...</div>
  <div class="loading-overlay"></div>
  </body>
</html>
