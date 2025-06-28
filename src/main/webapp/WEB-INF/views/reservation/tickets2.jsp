<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets-common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets-common2.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets2.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
</head>
<body>
<!-- 권종/할인 선택 -->
<div class="full-container">
    <jsp:include page="topbar.jsp" flush="false">
        <jsp:param name="type" value="2"/>
    </jsp:include>
    <div class="mainback-container">
        <div class="main-container">
            <div class="left-container">
                <div class="left-wrapper">
                    <div class="reservation-warning-container">
                        <div class="reservation-warning-wrapper">
                            <div id="reservation-warning-text">선택하신 좌석이 선점되었습니다. 10분 이내 결제하지 않으실 경우 선점된 좌석이 해제됩니다.</div>
                        </div>
                    </div>
                    <div class="ticket-selection-container">
                        <div class="ticket-selection-wrapper">
                            <div class="selection-infotext-container">
                                <div id="selection-infotext">티켓 종류 선택</div>
                            </div>
                            <div class="ticket-options-container">
                                <div class="ticket-options-wrapper">
                                    <!-- 성인 -->
                                    <div class="ticket-option-wrapper">
                                        <div class="option-text-wrapper">
                                            <div class="option-text">일반(성인)</div>
                                        </div>
                                        <div class="option-price-wrapper">
                                            <div class="option-price" id="price-adult">13000원</div>
                                        </div>
                                        <div class="option-cnt-wrapper">
                                            <div class="option-cnt">
                                                <select class="cnt-dropdown" id="cnt-adult" name="cnt-adult">
                                                    <!-- todo: 사용자가 선택한 매수에 따라 다르게 보이게 해야함 -->
                                                    <option value="0" selected>0매</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 청소년 -->
                                    <div class="ticket-option-wrapper">
                                        <div class="option-text-wrapper">
                                            <div class="option-text">청소년</div>
                                        </div>
                                        <div class="option-price-wrapper">
                                            <div class="option-price" id="price-youth">6500원</div>
                                        </div>
                                        <div class="option-cnt-wrapper">
                                            <div class="option-cnt">
                                                <select class="cnt-dropdown" id="cnt-youth" name="cnt-youth">
                                                    <!-- todo: 사용자가 선택한 매수에 따라 다르게 보이게 해야함 -->
                                                    <option value="0" selected>0매</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 어린이 -->
                                    <div class="ticket-option-wrapper">
                                        <div class="option-text-wrapper">
                                            <div class="option-text">어린이</div>
                                        </div>
                                        <div class="option-price-wrapper">
                                            <div class="option-price" id="price-kids">6500원</div>
                                        </div>
                                        <div class="option-cnt-wrapper">
                                            <div class="option-cnt">
                                                <select class="cnt-dropdown" id="cnt-kids" name="cnt-kids">
                                                    <!-- todo: 사용자가 선택한 매수에 따라 다르게 보이게 해야함 -->
                                                    <option value="0" selected>0매</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 경로(만 65세 이상) -->
                                    <div class="ticket-option-wrapper">
                                        <div class="option-text-wrapper">
                                            <div class="option-text">경로(만 65세 이상)</div>
                                        </div>
                                        <div class="option-price-wrapper">
                                            <div class="option-price" id="price-senior">6500원</div>
                                        </div>
                                        <div class="option-cnt-wrapper">
                                            <div class="option-cnt">
                                                <select class="cnt-dropdown" id="cnt-senior" name="cnt-senior">
                                                    <!-- todo: 사용자가 선택한 매수에 따라 다르게 보이게 해야함 -->
                                                    <option value="0" selected>0매</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 중증 장애인 -->
                                    <div class="ticket-option-wrapper">
                                        <div class="option-text-wrapper">
                                            <div class="option-text">중증 장애인</div>
                                        </div>
                                        <div class="option-price-wrapper">
                                            <div class="option-price" id="price-disabled">6500원</div>
                                        </div>
                                        <div class="option-cnt-wrapper">
                                            <div class="option-cnt">
                                                <select class="cnt-dropdown" id="cnt-disabled" name="cnt-disabled">
                                                    <!-- todo: 사용자가 선택한 매수에 따라 다르게 보이게 해야함 -->
                                                    <option value="0" selected>0매</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 군인(일반 사병) -->
                                    <div class="ticket-option-wrapper">
                                        <div class="option-text-wrapper">
                                            <div class="option-text">군인(일반 사병)</div>
                                        </div>
                                        <div class="option-price-wrapper">
                                            <div class="option-price" id="price-soldier">6500원</div>
                                        </div>
                                        <div class="option-cnt-wrapper">
                                            <div class="option-cnt">
                                                <select class="cnt-dropdown" id="cnt-soldier" name="cnt-soldier">
                                                    <!-- todo: 사용자가 선택한 매수에 따라 다르게 보이게 해야함 -->
                                                    <option value="0" selected>0매</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 국가 유공자 -->
                                    <div class="ticket-option-wrapper">
                                        <div class="option-text-wrapper">
                                            <div class="option-text">국가 유공자</div>
                                        </div>
                                        <div class="option-price-wrapper">
                                            <div class="option-price" id="price-national">6500원</div>
                                        </div>
                                        <div class="option-cnt-wrapper">
                                            <div class="option-cnt">
                                                <select class="cnt-dropdown" id="cnt-national" name="cnt-national">
                                                    <!-- todo: 사용자가 선택한 매수에 따라 다르게 보이게 해야함 -->
                                                    <option value="0" selected>0매</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="right-container">
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
                                            <img class="teamImg" id="team1Img" src="${pageContext.request.contextPath}/assets/img/teamlogos/1.png" alt="팀1 이미지">
                                        </div>
                                        <div class="teamName-wrapper">
                                            <div class="teamName" id="team1Name">신한 가디언즈</div>
                                        </div>
                                    </div>
                                    <div id="vs">VS</div>
                                    <div class="teamInfo-wrapper">
                                        <div class="teamImg-wrapper">
                                            <img class="teamImg" id="team1Img" src="${pageContext.request.contextPath}/assets/img/teamlogos/1.png" alt="팀1 이미지">
                                        </div>
                                        <div class="teamName-wrapper">
                                            <div class="teamName" id="team1Name">신한 가디언즈</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="right-gameInfo-container">
                                    <div class="right-gameInfo-wrapper">
                                        <div id="right-location">스타라이트 필드</div>
                                        <div id="right-date">2025-06-15(월) 18:30</div>
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
                                    <div id="zoneName">1루 퍼플석</div>
                                    <div id="seatDetail">A열 19번</div>
                                </div>
                                <div class="priceInfo-wrapper">
                                    <div id="priceInfotext">티켓 금액</div>
                                    <div id="price">13000</div>
                                </div>
                                <div class="usedPointInfo-wrapper">
                                    <div id="usedPointInfotext">사용 포인트</div>
                                    <div id="usedPoint">- 1000</div>
                                </div>
                                <div class="totalPayInfo-wrapper">
                                    <div id="totalPayInfotext">결제 금액</div>
                                    <div id="totalPay">14000</div>
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
                <div class="ticket-btn-container">
                    <div class="ticket-btn-wrapper">
                        <img id="backBtn" src="${pageContext.request.contextPath}/assets/img/reservation/backBtnSmall.svg" alt="이전버튼">
                        <img id="nextBtn" src="${pageContext.request.contextPath}/assets/img/reservation/nextBtnSmall.svg" alt="다음버튼">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/reservation/tickets2.js"></script>
</body>
</html>
