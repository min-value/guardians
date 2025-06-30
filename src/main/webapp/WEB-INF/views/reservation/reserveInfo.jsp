<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="right-wrapper">
  <div class="top-container">
    <div class="top-infotext-container">
      <div class="top-infotext-wrapper">
        <div id="infotext">경기 정보</div>
      </div>
    </div>
    <div class="top-infoImg-container">
      <div class="top-infoImg-wrapper">
        <div class="teamInfo-container">
          <div class="teamInfo-wrapper">
            <div class="teamImg-wrapper">
              <img class="teamImg" id="ourTeam" src="${pageContext.request.contextPath}/assets/img/teamlogos/6.png" alt="ourTeam">
            </div>
            <div class="teamName-wrapper">
              <div class="teamName" id="ourTeamName">신한 가디언즈</div>
            </div>
          </div>
          <div id="vs">VS</div>
          <div class="teamInfo-wrapper">
            <div class="teamImg-wrapper">
              <img class="teamImg" id="opponentTeam" src="${pageContext.request.contextPath}/assets/img/teamlogos/6.png" alt="팀1 이미지">
            </div>
            <div class="teamName-wrapper">
              <div class="teamName" id="opponentTeamName"></div>
            </div>
          </div>
        </div>
        <div class="right-gameInfo-container">
          <div class="right-gameInfo-wrapper">
            <div id="right-location">스타라이트 필드</div>
            <div id="right-date"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="bottom-container">
    <div class="bottom-wrapper">
      <div class="reserveInfo-container">
        <div class="bottom-infotext-wrapper">
          <div id="bottom-infotext">예매 정보</div>
        </div>
        <div class="selectedSeat-wrapper">
          <div id="zoneName"></div>
          <div id="seatDetail"></div>
        </div>
        <div class="priceInfo-wrapper">
          <div id="priceInfotext">티켓 금액</div>
          <div id="price"></div>
        </div>
        <div class="usedPointInfo-wrapper">
          <div id="usedPointInfotext">사용 포인트</div>
          <div id="usedPoint"></div>
        </div>
        <div class="totalPayInfo-wrapper">
          <div id="totalPayInfotext">결제 금액</div>
          <div id="totalPay"></div>
        </div>
      </div>
      <div class="cancelNotice-container">
        <div class="cancelNoticeText-wrapper">
          <div id="cancelNoticeText">※ 경기 1일 전까지 취소 가능<br>이후 취소 시 환불이 불가합니다.</div>
        </div>
        <div class="cancelAgree-container">
          <div class="cancelAgree-checkbox-wrapper">
            <input type="checkbox" id="cancelAgree-checkbox"><label for="cancelAgree-checkbox"></label>
            <div id="cancelAgree-text">취소 정책에 동의합니다</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>