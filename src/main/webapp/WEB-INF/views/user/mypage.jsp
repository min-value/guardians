<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String pageTitle = "마이페이지";
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/user/mypage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<%@ include file="../include/headerImg.jsp" %>

<div class="mypage-container">
    <div class="mypage-tabs">
        <button class="tab-btn active" data-tab="info">내 정보</button>
        <button class="tab-btn" data-tab="tickets">예매내역</button>
        <button class="tab-btn" data-tab="points">포인트내역</button>
        <button class="tab-btn" data-tab="fairy">승리요정</button>
    </div>

    <div id="tab-content-container"></div>
</div>
<%@ include file="/WEB-INF/views/include/footer.jsp" %>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const tabs = document.querySelectorAll('.mypage-tabs .tab-btn');
        const contentContainer = document.getElementById('tab-content-container');

        // 내 정보 조회, 수정
        function bindInfoForm() {
            const form = document.getElementById("infoForm");
            const editBtn = document.getElementById("editBtn");
            const email = document.getElementById('email');
            const phoneNumber = document.getElementById('phoneNumber');
            const iconHTML = `<img src="/assets/img/user/error-message.svg" alt="에러 아이콘">`;

            const regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
            const regPhone = /^(01[016789]|02|0[3-9][0-9])-[0-9]{3,4}-[0-9]{4}$/;

            const openModalBtn = document.getElementById("openWithdrawModal");
            const modal = document.getElementById("withdrawModal");
            const cancelBtn = document.getElementById("cancelWithdraw");
            const agreeCheckbox = document.getElementById("agreeCheckbox");
            const confirmWithdrawBtn = document.getElementById("confirmWithdraw");

            // 이메일 유효성 검사
            function validEmail(){
                const checkEmail = email.value.trim();
                const emailError = document.getElementById('email-error');

                if (checkEmail === "") {
                    emailError.innerHTML = "";
                    return false;
                }
                if(!regEmail.test(checkEmail)){
                    emailError.innerHTML= iconHTML + '이메일 형식에 맞지 않습니다.';
                    return false;
                }
                emailError.innerHTML = '';
                return true;
            }

            // 전화번호 유효성 검사
            function validPhone(){
                const checkPhone = phoneNumber.value.trim();
                const phoneError = document.getElementById('phone-error');

                if (checkPhone === "") {
                    phoneError.innerHTML = "";
                    return false;
                }
                if(!regPhone.test(checkPhone)){
                    phoneError.innerHTML= iconHTML + '전화번호 형식에 맞지 않습니다.';
                    return false;
                }
                phoneError.innerHTML = '';
                return true;
            }

            if (openModalBtn && modal && cancelBtn && agreeCheckbox && confirmWithdrawBtn) {
                // 모달 열기
                openModalBtn.addEventListener("click", function (e) {
                    e.preventDefault();
                    modal.style.display = "block";
                });

                // 모달 닫기
                cancelBtn.addEventListener("click", function () {
                    modal.style.display = "none";
                    agreeCheckbox.checked = false;
                    confirmWithdrawBtn.disabled = true;
                });

                // 회원 탈퇴
                confirmWithdrawBtn.addEventListener("click", () => {
                    if (!agreeCheckbox.checked) return;

                    fetch("/user/delete", {
                        method: "POST",
                    })
                        .then(res => res.json())
                        .then(data => {
                            alert(data.message);
                            if (data.success) {
                                window.location.href = "/user/logout";
                            }
                        })
                        .catch(err => {
                            alert("탈퇴 중 오류가 발생했습니다.");
                            console.error(err);
                        });
                });

                // 동의 체크 여부에 따라 버튼 활성화
                agreeCheckbox.addEventListener("change", function () {
                    confirmWithdrawBtn.disabled = !agreeCheckbox.checked;
                });
            }

            // 모든 입력 & 유효성 통과 여부 확인해서 버튼 활성화
            function checkInputs() {
                const emailOK = validEmail();
                const phoneOK = validPhone();

                editBtn.disabled = !(emailOK && phoneOK);
            }

            [email, phoneNumber].forEach(el => {
                el.addEventListener('input', checkInputs);
            });

            if (!form || !editBtn) return;
            let isEditMode = false;
            editBtn.addEventListener("click", () => {
                if (!isEditMode) {
                    form.querySelectorAll("input").forEach(input => {
                        if (input.name !== "userId") input.removeAttribute("readonly");
                    });
                    editBtn.textContent = "저장";
                    editBtn.classList.replace("edit-mode", "save-mode");
                    isEditMode = true;
                } else {

                    if(!validEmail() || !validPhone()){
                        return;
                    }
                    const formData = new FormData(form);
                    fetch('/user/update', {
                        method: 'POST',
                        body: new URLSearchParams(formData)
                    })
                        .then(res => res.json())
                        .then(data => {
                            alert(data.message);
                            if (data.success) {
                                form.querySelectorAll("input").forEach(input => input.setAttribute("readonly", true));
                                editBtn.textContent = "수정";
                                editBtn.classList.replace("save-mode", "edit-mode");
                                isEditMode = false;
                            }
                        })
                        .catch(err => {
                            alert("서버 오류가 발생했습니다.");
                            console.error(err);
                        });
                }
            });
        }

        // 예매내역 조회
        function bindTickets(page = 1) {
            const tryRender = () => {
                const container = document.getElementById('tickets');
                if (!container) {
                    setTimeout(() => bindTickets(page), 100);
                    return;
                }

                fetch('/user/tickets')
                    .then(res => res.json())
                    .then(tickets => {

                        const pageSize = 5;
                        const totalCount = tickets.length;
                        const start = (page - 1) * pageSize;
                        const end = start + pageSize;
                        const pageItems = tickets.slice(start, end);

                        if (!totalCount) {
                            container.innerHTML = '<p class="no-tickets-msg">예매내역이 없습니다.</p>';
                            $(paginationContainerId).empty();
                            return;
                        }

                        container.innerHTML = '';
                        tickets.forEach(ticket => {
                            const item = document.createElement('div');
                            item.classList.add('ticket-item');

                            const gameDate = new Date(ticket.gameDate);
                            const now = new Date();

                            const isCancelable = gameDate.getTime() > now.getTime();
                            const isCanceled = ticket.cancel === true;
                            const cancelBtnId = "cancel-" + ticket.ticketNumber;

                            let cancelBtn = '';
                            if(isCanceled){
                                cancelBtn = `<div class="cancel-container">
                                        <button class="used-btn" disabled>취소 완료</button></div>`;
                            }else if(!isCanceled && isCancelable){
                                cancelBtn = `<div class="cancel-container">
                                        <button class="cancel-btn"
                                        data-ticket="\${ticket.ticketNumber}"
                                        data-impuid="\${ticket.imp_uid}"
                                        data-userpk="\${ticket.userPk}"
                                        data-reservepk="\${ticket.ticketNumber}"
                                        data-usedpoint="\${ticket.used_point}"
                                        data-gamedate="\${ticket.gameDate}"
                                        id="\${cancelBtnId}">예매취소</button></div>`;
                            }else{
                                cancelBtn = `<div class="cancel-container">
                                        <button class="used-btn" disabled>사용 완료</button></div>`;
                            }

                            item.innerHTML = `
                                <div class="ticket-top">
                                    <img src="/assets/img/teamlogos/\${ticket.homeTeamPk}.svg" class="logo-team">
                                    <span class="vs-text">VS</span>
                                    <img src="/assets/img/teamlogos/\${ticket.oppTeamPk}.svg" class="logo-team">
                                    <span class="match-date">\${ticket.matchDate}</span>
                                    <img src="/assets/img/icon/chevron-right.svg" class="arrow-icon">
                                </div>
                                <div class="ticket-info">
                                       <div class="ticket-info-main">
                                          <div class="ticket-details">
                                            <div class="info-row">
                                              <span class="label">예매자</span>
                                              <span class="user">\${ticket.userName}</span>
                                            </div>
                                            <div class="info-row">
                                              <span class="label">예매일</span>
                                              <span class="ticket-num">\${ticket.reserveDate}</span>
                                            </div>
                                            <div class="info-row">
                                              <span class="label">예매번호</span>
                                              <span class="ticket-num">\${ticket.ticketNumber}</span>
                                            </div>
                                            <div class="info-row">
                                              <span class="label">경기장</span>
                                              <span class="stadium">\${ticket.stadium}</span>
                                            </div>
                                            <div class="info-row">
                                              <span class="label">좌석번호</span>
                                              <span class="seat">\${ticket.seatInfo.replace(/, /g, '<br>')}</span>
                                            </div>
                                          </div>
                                          \${cancelBtn}
                                        </div>
                                </div>
                              `;
                            container.appendChild(item);

                            const arrow = item.querySelector('.arrow-icon');
                            arrow.addEventListener('click', () => {
                                item.classList.toggle('expanded');
                            });

                            // 예매 취소 버튼 클릭 이벤트
                            if (isCancelable && !isCanceled) {
                                const cancelBtn = document.getElementById(cancelBtnId);
                                cancelBtn.addEventListener("click", () => {
                                    const imp_uid = cancelBtn.dataset.impuid;
                                    const user_pk = cancelBtn.dataset.userpk;
                                    const reservelist_pk = cancelBtn.dataset.ticket;
                                    const used_point = cancelBtn.dataset.usedpoint;
                                    const game_date = new Date(cancelBtn.dataset.gamedate);
                                    const now = new Date();
                                    const gameDay = new Date(gameDate.getFullYear(), gameDate.getMonth(), gameDate.getDate());
                                    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                                    $.ajax({
                                        type: "POST",
                                        url: "/user/cancel",
                                        data: {
                                            user_pk: user_pk,
                                            reservelist_pk: reservelist_pk,
                                            point: used_point
                                        },
                                        success: function (res) {
                                            alert("취소 처리 완료");
                                            if(gameDay>today){
                                                $.ajax({
                                                    type: "POST",
                                                    url: "/cancelPayment/" + imp_uid,
                                                    success: function (response) {
                                                        alert("결제 취소 완료");
                                                    },
                                                    error: function (error) {
                                                        alert("결제 취소 실패");
                                                        console.error(error);
                                                    }
                                                });
                                            }
                                            document.querySelector("")
                                            location.reload();
                                        },
                                        error: function (error) {
                                            alert("취소 처리 실패");
                                            console.error(error);
                                        }
                                    });
                                });
                            }
                        });
                        // 페이지네이션
                        createPagination({
                            currentPage: page,
                            totalCount,
                            pageSize,
                            containerId: '#pagination',
                            onPageChange: (newPage) => {
                                bindTickets(newPage);
                            }
                        });
                    });
            };

            tryRender();
        }

        // 포인트 내역 조회
        function bindPoints(page = 1) {
            const tryRender = () => {
                const container = document.querySelector('.points-list');
                const totalDiv = document.querySelector('.point-total');
                const paginationContainerId = '#points-pagination';
                const pageSize = 10;

                if (!container || !totalDiv) {
                    setTimeout(() => bindPoints(page), 100);
                    return;
                }

                Promise.all([
                    fetch('/point/list').then(res => res.json()),
                    fetch('/user/point/total').then(res => res.json())
                ]).then(([points, totalAmount]) => {
                    const totalDiv = document.querySelector('.point-total');
                    const totalCount = points.length;
                    const start = (page - 1) * pageSize;
                    const end = start + pageSize;
                    const pageItems = points.slice(start, end);

                    if (totalDiv) {
                        totalDiv.textContent = totalAmount + 'P';
                    }

                    if (!points.length) {
                        container.innerHTML = '<p class="no-points-msg">포인트 내역이 없습니다.</p>';
                        $(paginationContainerId).empty();
                        return;
                    }

                    container.innerHTML = '';
                    let lastDate = '';

                    pageItems.forEach(point => {
                        if (point.formattedDate !== lastDate) {
                            const group = document.createElement('div');
                            group.className = 'date-group';
                            container.appendChild(group);

                            const dateDiv = document.createElement('div');
                            dateDiv.className = 'point-date';
                            dateDiv.textContent = point.formattedDate;
                            group.appendChild(dateDiv);

                            lastDate = point.formattedDate;
                        }

                        const group = container.lastElementChild;
                        const item = document.createElement('div');
                        item.className = 'point-item';
                        item.innerHTML = `
                                <div class="detail-wrapper">
                                    <div class="point-type">\${point.type}</div>
                                    <div class="point-description">\${point.description}</div>
                                </div>
                                <span class="point-amount">\${point.point}P</span>
                            `;
                        const amountSpan = item.querySelector('.point-amount');

                        if (point.point < 0) {
                            amountSpan.classList.add('negative');
                            amountSpan.textContent = `\${point.point}P`;
                        } else {
                            amountSpan.textContent = `+\${point.point}P`;
                        }
                        group.appendChild(item);
                    });

                    // 페이지네이션
                    createPagination({
                        currentPage: page,
                        totalCount: totalCount,
                        pageSize: pageSize,
                        containerId: paginationContainerId,
                        onPageChange: (newPage) => {
                            bindPoints(newPage);
                        }
                    });
                }).catch(err => {
                    console.error("포인트 데이터 로딩 오류:", err);
                });
            };
            tryRender();
        }

        // 승리요정
        function bindFairy(page = 1) {
            const pageSize = 5;
            const paginationContainerId = '#fairy-pagination';
            fetch('/user/fairy/data')
                .then(res => res.json())
                .then(fairy => {
                    document.querySelector('.stat:nth-child(1) .count').textContent = fairy.totalCnt + '회';
                    document.querySelector('.stat:nth-child(2) .count').textContent = fairy.winCnt + '회';
                    document.querySelector('.stat:nth-child(3) .count').textContent = fairy.drawCnt + '회';
                    document.querySelector('.stat:nth-child(4) .count').textContent = fairy.loseCnt + '회';
                    document.querySelector('.stat:nth-child(5) .count').textContent = fairy.winRateFormatted;
                });

            const tryRender = () => {
                const container = document.getElementById('fairy');
                if (!container) {
                    setTimeout(() => bindFairy(page), 100);
                    return;
                }

                fetch('/user/tickets')
                    .then(res => res.json())
                    .then(tickets => {
                        const now = new Date();
                        const pastTickets = tickets.filter(ticket => new Date(ticket.gameDate).getTime() <= now.getTime());

                        const totalCount = pastTickets.length;
                        const start = (page - 1) * pageSize;
                        const end = start + pageSize;
                        const pageItems = pastTickets.slice(start, end);

                        if (!pastTickets.length) {
                            container.innerHTML = '<p class="no-fairy-msg">직관 내역이 없습니다.</p>';
                            $(paginationContainerId).empty();
                            return;
                        }

                        container.innerHTML = '';
                        pageItems.forEach(ticket => {
                            const item = document.createElement('div');
                            item.classList.add('fairy-item');

                            item.innerHTML = `
                                <div class="fairy-top">
                                    <span class ="predict-text"></span>
                                    <img src="/assets/img/teamlogos/\${ticket.homeTeamPk}.svg" class="logo-team">
                                    <span class="vs-text">VS</span>
                                    <img src="/assets/img/teamlogos/\${ticket.oppTeamPk}.svg" class="logo-team">
                                    <span class="match-date">\${ticket.matchDate}</span>
                                    <img src="/assets/img/icon/chevron-right.svg" class="arrow-icon">
                                </div>
                                <div class="fairy-info"></div>
                            `;

                            const fairyInfo = item.querySelector('.fairy-info');
                            if (ticket.result === 'CANCEL') {
                                fairyInfo.innerHTML = `<div class="cancel-msg">취소 경기는 경기 세부 내역이 없습니다.</div>`;
                            } else {
                                fairyInfo.innerHTML = `
                                <div class="fairy-bar-source" style="display: none;">
                                    <div class="hit">안타: \${ticket.our_hit} / \${ticket.opp_hit}</div>
                                    <div class="homerun">홈런: \${ticket.our_homerun} / \${ticket.opp_homerun}</div>
                                    <div class="strike-out">삼진: \${ticket.our_strikeout} / \${ticket.opp_strikeout}</div>
                                    <div class="bb">4사구: \${ticket.our_bb} / \${ticket.opp_bb}</div>
                                    <div class="miss">실책: \${ticket.our_miss} / \${ticket.opp_miss}</div>
                                </div>
                                <div class="fairy-bar-container"></div>
                            </div>
                        `;
                            }

                            const predictText = item.querySelector('.predict-text');
                            predictText.classList.add('predict-text');

                            if (ticket.result === 'CANCEL') {
                                predictText.textContent = '경기 취소';
                                predictText.classList.add('predict-cancel');
                            } else if (ticket.result === 'DRAW') {
                                predictText.textContent = '무승부';
                                predictText.classList.add('predict-draw');
                            } else if (ticket.result === 'WIN' || ticket.result === 'LOSE') {
                                const isSuccess =
                                    (ticket.result === 'WIN' && ticket.predict === false) ||
                                    (ticket.result === 'LOSE' && ticket.predict === true);

                                predictText.textContent = isSuccess ? '예측 성공' : '예측 실패';
                                predictText.classList.add(isSuccess ? 'predict-success' : 'predict-fail');
                            }

                            const detail = item.querySelector('.fairy-bar-source');
                            const chart = item.querySelector('.fairy-bar-container');

                            if (detail && chart) {
                                drawFairyChart(detail, chart);
                            }

                            const arrow = item.querySelector('.arrow-icon');
                            arrow.addEventListener('click', () => {
                                item.classList.toggle('expanded');
                            });

                            container.appendChild(item);
                        });

                        // 페이지네이션
                        createPagination({
                            currentPage: page,
                            totalCount: totalCount,
                            pageSize: pageSize,
                            containerId: paginationContainerId,
                            onPageChange: (newPage) => {
                                bindFairy(newPage);
                            }
                        });
                    });
            };

            tryRender();
        }

        function drawFairyChart(detailElem, containerElem) {
            const classes = ['hit', 'homerun', 'strike-out', 'bb', 'miss'];
            const labels = ['안타', '홈런', '삼진', '4사구', '실책'];
            const PIXEL_PER_UNIT = 10

            const ourData = [], oppData = [];
            classes.forEach(cn => {
                const el = detailElem.getElementsByClassName(cn)[0];
                if (!el || !el.textContent.includes(':')) {
                    ourData.push(0);
                    oppData.push(0);
                } else {
                    const [o, p] = el.textContent
                        .split(':')[1]
                        .split('/')
                        .map(x => parseInt(x.trim(), 10));
                    ourData.push(isNaN(o) ? 0 : o);
                    oppData.push(isNaN(p) ? 0 : p);
                }
            });

            containerElem.innerHTML = '';

            // 3) 그리드 row 단위로 DOM 조합하기
            ourData.forEach((o, i) => {
                const p = oppData[i];
                const ourW = o * PIXEL_PER_UNIT;
                const oppW = p * PIXEL_PER_UNIT;

                const row = document.createElement('div');
                row.className = 'bar-row';

                // 왼쪽 막대
                const lw = document.createElement('div');
                lw.className = 'bar-wrap';
                lw.style.width = ourW + 'px';
                const lb = document.createElement('div');
                lb.className = 'bar our';
                lb.style.width = ourW + 'px';
                lw.appendChild(lb);

                // 왼쪽 숫자
                const lv = document.createElement('span');
                lv.className = 'bar-value';
                lv.textContent = o;

                // 레이블
                const lg = document.createElement('div');
                lg.className = 'bar-label';
                lg.textContent = labels[i];

                // 오른쪽 숫자
                const rv = document.createElement('span');
                rv.className = 'bar-value';
                rv.textContent = p;

                // 오른쪽 막대
                const rw = document.createElement('div');
                rw.className = 'bar-wrap';
                rw.style.width = oppW + 'px';
                const rb = document.createElement('div');
                rb.className = 'bar opp';
                rb.style.width = oppW + 'px';
                rw.appendChild(rb);

                // 그리드 조합
                row.append(lw, lv, lg, rv, rw);
                containerElem.appendChild(row);
            });
        }

        function loadTabContent(tabName) {
            const url = '/user/mypage/' + tabName;
            fetch(url)
                .then(res => res.text())
                .then(html => {
                    contentContainer.innerHTML = html;

                    if (tabName === 'info') {
                        bindInfoForm();
                    } else if (tabName === 'tickets') {
                        bindTickets();
                    } else if (tabName === 'points') {
                        bindPoints();
                    } else if (tabName === 'fairy') {
                        bindFairy();
                    }
                })
                .catch(err => console.error(err));
        }


        // 초기 로드
        loadTabContent('info');

        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                const name = tab.getAttribute('data-tab');
                tabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                loadTabContent(name);
            });
        });
    });
</script>


</body>
</html>
