<%@ page import="org.baseball.dto.UserDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Integer reservelistPk = (Integer) session.getAttribute("reservelistPk");
    UserDTO user = (UserDTO) session.getAttribute("loginUser");
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tickets/predict.css">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script>
      let curPredict = null;
      let reservelistPk = "<%= reservelistPk != null ? reservelistPk : "null" %>";
      let userPk = "<%= user != null ? user.getUserPk() : "null" %>";

      $(document).ready(function () {
        if(reservelistPk==="null") {
          document.querySelector('.modal').style.display = 'none';
        }

        $("#ourTeam").on("mouseenter", function () {
          $(this).css({ opacity: 1, height: "170px" });
          $("#opponentTeam").css({ opacity: 0.3, height: "150px" });
        }).on("mouseleave", function () {
          if(curPredict == null) {
            $(this).css({opacity: 1, height: "150px"});
            $("#opponentTeam").css({ opacity: 1, height: "150px" });
          }else if (curPredict === 1) {
            $(this).css({ opacity: 0.3, height: "150px" });
            $("#opponentTeam").css({ opacity: 1, height: "170px" });
          }
        });

        $("#opponentTeam").on("mouseenter", function () {
          $(this).css({ opacity: 1, height: "170px" });
          $("#ourTeam").css({ opacity: 0.3, height: "150px" });
        }).on("mouseleave", function () {
          if(curPredict == null) {
            $(this).css({opacity: 1, height: "150px"});
            $("#ourTeam").css({ opacity: 1, height: "150px" });
          }else if (curPredict === 0) {
            $(this).css({ opacity: 0.3, height: "150px" });
            $("#ourTeam").css({ opacity: 1, height: "170px" });
          }
        });

        $("#ourTeam").on("click", function () {
          curPredict = 0;

          const target = document.querySelector(".modal-btn");
          target.disabled = false;
          target.style.backgroundColor = "var(--mainColor)";
          target.style.color = "var(--white)";
        });

        $("#opponentTeam").on("click", function () {
          curPredict = 1;

          const target = document.querySelector(".modal-btn");
          target.disabled = false;
          target.style.backgroundColor = "var(--mainColor)";
          target.style.color = "var(--white)";
        });

          console.log(curPredict);
          console.log(reservelistPk);
      });

      function clickCheerBtn() {
          console.log(curPredict);
          console.log(reservelistPk);
        $.ajax({
          url: '/tickets/predict',
          method: 'POST',
          data: {
            userPk : userPk,
            reservelistPk : reservelistPk,
            predict: curPredict,
          },
          success: function (res) {
            alert("예측 완료!");
            document.querySelector('.modal').style.display = 'none';
          },
          error: function (err) {
            alert("데이터를 저장하지 못했습니다. 다시 시도해주세요.");
            console.error(err);
          }
        });
      }

    </script>
</head>
<body>
  <div class="modal">
    <div class="modal-container">
      <div class="modal-header">승부예측</div>
      <div class="modal-body">
        <div class="text1">예매가 성공적으로 완료되었습니다.</div>
        <div class="text2">경기 결과를 맞추고 포인트 받아가세요!</div>
          <div class="team-containers">
            <div class="logo-container">
              <img class="teamlogos" src="/assets/img/teamlogos/6.png" alt="ourTeam" id="ourTeam">
            </div>
            vs
            <div class="logo-container">
              <img class="teamlogos" src="/assets/img/teamlogos/5.png" alt="opponentTeam" id="opponentTeam">            </div>
            </div>
        <input class="modal-btn" type="button" value="응원하기" onclick="clickCheerBtn()" disabled>
        <div class="text3">결과를 맞추면 예매 금액의 8%, 틀리면 2%를 포인트로 드려요</div>
      </div>
    </div>
  </div>
</body>
</html>
