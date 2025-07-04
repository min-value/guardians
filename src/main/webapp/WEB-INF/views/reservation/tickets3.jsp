<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets-common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets-common2.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/tickets3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation/loading.css">
    <script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
</head>
<script>
    const gamePk = Number(${gamePk});
    const gameInfo = JSON.parse(localStorage.getItem('gameInfo' + gamePk));

    const user = {
        userPk: "${sessionScope.loginUser.userPk}",
        userName: "${sessionScope.loginUser.userName}",
        tel: "${sessionScope.loginUser.tel}",
        email: "${sessionScope.loginUser.email}",
        totalPoint: "${sessionScope.loginUser.totalPoint}"
    };

    $.ajax({
        type: "GET",
        url: "/user/info",
        success: function(user) {
            console.log("최신 유저 정보:", user);
            $("#pointDisplay").text(user.totalPoint);
            window.user = user;
        },
        error: function(err) {
            console.error("유저 정보 가져오기 실패", err);
        }
    });

    function validatePointInput(input) {
        let value = input.value.replace(/[^0-9]/g, '');
        const myPoint = parseInt(document.getElementById('myPoint').innerText.replace(/[^0-9]/g, ''));
        const ticketPrice = parseInt(document.getElementById('price').innerText.replace(/[^0-9]/g, ''));
        const availablePoint = parseInt(ticketPrice*0.1);

        if (parseInt(value) > myPoint) {
            value = myPoint;
            document.getElementById("pointMsg").innerText = "포인트가 부족합니다";
        }
        if (parseInt(value) > availablePoint) {
            value = availablePoint;
            document.getElementById("pointMsg").innerText = "포인트는 결제금액의 10%만 사용 사능합니다";
        }

        input.value = value;

        document.getElementById('usedPoint').innerText = `- ` + value;
        document.getElementById('totalPay').innerText = ticketPrice - parseInt(value || 0);
    }

</script>

<body>
<!-- 권종/할인 선택 -->
<div class="full-container">
    <jsp:include page="topbar.jsp" flush="false">
        <jsp:param name="type" value="3"/>
    </jsp:include>
    <div class="mainback-container">
        <div class="main-container">
            <div class="left-container">
                <!-- 상세 보기 -->
                <!-- 2 -->
                <div class="notification-container" id="noti2">
                    <div class="notification-wrapper">
                        <div class="notification-title-container">
                            <div class="notification-title-wrapper">
                                <div class="notification-title">
                                    개인정보 제3자 정보제공
                                </div>
                            </div>
                            <div class="closeBtn-wrapper">
                                <img class="closeBtn" src="${pageContext.request.contextPath}/assets/img/reservation/closeBtn.svg" alt="닫기 버튼">
                            </div>
                        </div>
                        <div class="notification-content-container">
                            <div class="notification-content-wrapper">
                                <div class="notification-content">
                                    <p>엔에이치엔링크㈜가 제공하는 상품 및 서비스를 구매하고자 할 경우, NHN LINK는 고객응대 및 상품정보 안내 등을 위하여 필요한 최소한의 개인정보만을 아래와 같이 제공하고 있습니다. NHN LINK는 정보통신망 이용촉진 및 정보보호 등에 관한 법률에 따라 아래와 같이 개인정보 제공에 대한 사항을 안내 드리오니 자세히 읽은 후 동의하여 주시기 바랍니다.</p>
                                    <br><br>
                                    <p>
                                        <strong>1. 개인정보를 제공받는 자</strong>
                                        - 신한 guardians(구단)
                                        <br><br>
                                        <strong>2. 제공하는 개인정보 항목</strong>
                                        - 신한 guardians(구단) : 이름, 생년월일, 아이디, 휴대폰번호, (제공 시)이메일 주소, (배송 시)주문/배송정보
                                        <br><br>
                                        <strong>3. 개인정보를 제공받는 자의 이용목적</strong>
                                        - 신한 guardians(구단) : 티켓현장발권, 예매서비스 제공에 따른 내역 관리이행 등 티켓 예매 대행, 민원처리 등 고객상담, 서비스 분석과 통계에 따른 혜택 및 맞춤 서비스 제공, 서비스 이용에 따른 설문조사 및 혜택제공
                                        <br><br>
                                        <strong>4. 개인정보를 제공받는 자의 개인정보 보유 및 이용기간</strong>
                                        - 신한 guardians(구단) : 회원탈퇴 시 또는 개인정보 이용목적 달성 시까지. 단, 관계법령의 규정에 의해 보존의 필요가 있는 경우 및 사전 동의를 득한 경우 해당 보유기간까지
                                        <br><br>
                                        <strong>5. 동의거부권 등에 대한 고지</strong>
                                        - 본 개인정보 제공에 동의하지 않으시는 경우, 동의를 거부할 수 있으며, 이 경우 상품구매가 제한될 수 있습니다.
                                        <br><br>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 3 -->
                <div class="notification-container" id="noti3">
                    <div class="notification-wrapper">
                        <div class="notification-title-container">
                            <div class="notification-title-wrapper">
                                <div class="notification-title">
                                    KBO리그 SAFE 캠페인
                                </div>
                            </div>
                            <div class="closeBtn-wrapper">
                                <img class="closeBtn" src="${pageContext.request.contextPath}/assets/img/reservation/closeBtn.svg" alt="닫기 버튼">
                            </div>
                        </div>
                        <div class="notification-content-container">
                            <div class="notification-content-wrapper">
                                <div class="notification-content">
                                    <p><strong>&lt;SAFE 캠페인이란&gt;</strong></p>
                                    <p>
                                        - SAFE 캠페인이란 안전하고 쾌적한 야구장 환경을 조성하고, 성숙한 관람 문화의 정착을 돕기 위해
                                        Security(안전), Attention(주의), Fresh(쾌적), Emergency(응급상황) 등 다양한 안전관람수칙을 공유하는 한편
                                        2015 시즌부터 적용되는 경기장 안전 ∙ 보안 강화 규정을 팬 여러분께 알리기 위한 야구 관람 안전 캠페인입니다.
                                    </p>

                                    <br>

                                    <p><strong>&lt;안전 • 보안 규정의 주요 내용&gt;</strong></p>
                                    <p>
                                        - 선수와 관람객의 안전을 위협할 수 있는 모든 유리병, 총량 1L 초과 PET 및 알루미늄 캔 용기에 담긴 음료
                                        (미개봉 상태의 음료에 한해 1인당 PET 1개, 캔 2개까지 허용), 얼린 생수의 경기장 내 반입이 제한됩니다.
                                    </p>
                                    <p>
                                        - 과도한 물품의 반입으로 인해 관람객의 이동과 통행을 방해하고 경기장 내 시설물(좌석∙컵홀더 등)을 활용하여
                                        거치하는 등 타인의 경기장 이용에 지장을 초래하거나, 유사시 원활한 대피를 어렵게 하는 일이 발생하지 않도록
                                        반입 가능한 소지품의 품목, 크기와 개수가 제한됩니다.
                                    </p>
                                    <p>
                                        - 관람객 1인당 가방 1개(가로 45cm x 세로 45cm x 폭 20cm)와 쇼핑백류 1개(가로 30cm x 손잡이 포함 세로 50cm x 폭 12cm)까지
                                        지참이 가능하며, 그 외의 가방, 상자 • 아이스박스를 비롯한 돗자리, 휴대용 의자, 휴대용 간이테이블과 같은 물품들은
                                        반입이 허용되지 않습니다.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 4 -->
                <div class="notification-container" id="noti4">
                    <div class="notification-wrapper">
                        <div class="notification-title-container">
                            <div class="notification-title-wrapper">
                                <div class="notification-title">
                                    프로스포츠 암표매매 행위에 따른 제재사항 안내
                                </div>
                            </div>
                            <div class="closeBtn-wrapper">
                                <img class="closeBtn" src="${pageContext.request.contextPath}/assets/img/reservation/closeBtn.svg" alt="닫기 버튼">
                            </div>
                        </div>
                        <div class="notification-content-container">
                            <div class="notification-content-wrapper" style="overflow-y: hidden">
                                <div class="notification-content">
                                    <img id="sportsNotice" src="${pageContext.request.contextPath}/assets/img/reservation/sportsNotice.png" alt="암표매매 제재사항 안내">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="left-wrapper">
                    <div class="orderInfo-container">
                        <div class="orderInfo-text-container">
                            <div id="orderInfo-text">주문자 정보</div>
                        </div>
                        <div class="orderInfo-detail-container">
                            <div class="orderInfo-detail-wrapper">
                                <div class="orderInfo1">
                                    <div class="buyerName-container">
                                        <div class="buyerName-text-wrapper">
                                            <div id="buyerName-text">이름</div>
                                        </div>
                                        <div class="buyerName-detail-wrapper">
                                            <div class="buyerName-detail">
                                                <div id="buyerName">${loginUser.userName}</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="buyerPhone-container">
                                        <div class="buyerPhone-text-wrapper">
                                            <div id="buyerPhone-text">휴대전화</div>
                                        </div>
                                        <div class="buyerPhone-detail-wrapper">
                                            <div class="buyerPhone-detail">
                                                <div id="buyerPhone">${loginUser.tel}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="orderInfo2">
                                    <div class="buyerEmail-container">
                                        <div class="buyerEmail-text-wrapper">
                                            <div id="buyerEmail-text">이메일</div>
                                        </div>
                                        <div class="buyerEmail-detail-wrapper">
                                            <div class="buyerEmail-detail">
                                                <div id="buyerEmail">${loginUser.email}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="orderConfirm-container">
                        <div class="orderConfirm-text-container">
                            <div id="orderConfirm-text">예매자 확인</div>
                        </div>
                        <div class="orderConfirm-detail-container">
                            <div class="orderConfirm-detail-wrapper">
                                <div class="orderConfirm-checkbox-wrapper">
                                    <input type="checkbox" class="orderConfirm-checkbox" id="confirm1-checkbox"><label for="confirm1-checkbox"></label>
                                    <div class="orderConfirm-checkbox-text">주문자 확인 및 예매처리를 위해 휴대폰번호, 이메일, (배송수령 시)주소, <br>(입력필요 시)생년월일을 수집하며,  이용목적 달성 이후 파기합니다.</div>
                                </div>
                            </div>
                            <div class="orderConfirm-detail-wrapper">
                                <div class="orderConfirm-checkbox-wrapper">
                                    <input type="checkbox" class="orderConfirm-checkbox" id="confirm2-checkbox"><label for="confirm2-checkbox"></label>
                                    <div class="orderConfirm-checkbox-text">
                                        개인정보 제 3자 제공에 동의합니다. (고객응대 및 관람정보안내 등을 위함)
                                        <div class="viewDetail notification" id="notification2">상세보기</div>
                                    </div>
                                </div>
                            </div>
                            <div class="orderConfirm-detail-wrapper">
                                <div class="orderConfirm-checkbox-wrapper">
                                    <input type="checkbox" class="orderConfirm-checkbox" id="confirm3-checkbox"><label for="confirm3-checkbox"></label>
                                    <div class="orderConfirm-checkbox-text">
                                        KBO리그 SAFE캠페인에 동의합니다.
                                        <div class="viewDetail notification" id="notification3">상세보기</div>
                                    </div>
                                </div>
                            </div>
                            <div class="orderConfirm-detail-wrapper">
                                <div class="orderConfirm-checkbox-wrapper">
                                    <input type="checkbox" class="orderConfirm-checkbox" id="confirm4-checkbox"><label for="confirm4-checkbox"></label>
                                    <div class="orderConfirm-checkbox-text">
                                        프로스포츠 암표매매 행위에 따른 제재사항에 동의합니다.
                                        <div class="viewDetail notification" id="notification4">상세보기</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="paymentInfo-container">
                        <div class="paymentInfo-text-container">
                            <div id="paymentInfo-text">포인트 사용</div>
                        </div>
                        <div class="paymentInfo-detail-container">
                            <div class="paymentInfo-detail-wrapper">
                                <div class="paymentInfo-card-container">
                                    <div class="paymentInfo-card-wrapper">
                                        <img id="paymentInfo-card" src="${pageContext.request.contextPath}/assets/img/reservation/homeRunPay.svg" alt="홈런페이">
                                        <div id="myPoint"></div>
                                    </div>
                                </div>
                                <div class="paymentInfo-point-container">
                                    <div class="pointInfo-text-container">
                                        <div id="pointInfo-text">사용하실 포인트를 입력해주세요. </div>
                                    </div>
                                    <div class="point-container">
                                        <div id="point">
                                            <input type="text" id="usePoint"
                                                   oninput="validatePointInput(this)"/>
                                            <span>원</span>
                                        </div>
                                        <div id="pointMsg"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="right-container">
                <%@ include file="/WEB-INF/views/reservation/reserveInfo.jsp"%>
                <div class="ticket-btn-container">
                    <div class="ticket-btn-wrapper">
                        <img src="${pageContext.request.contextPath}/assets/img/reservation/backBtnSmall.svg" alt="이전버튼" id="beforeBtn">
                        <img src="${pageContext.request.contextPath}/assets/img/reservation/payBtn.svg" alt="결제버튼" id="payBtn" >
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="loader">Loading...</div>
<div class="loading-overlay"></div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/reservation/tickets3.js"></script>
</body>
</html>
