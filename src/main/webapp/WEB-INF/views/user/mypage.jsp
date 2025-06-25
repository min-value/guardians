<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>마이페이지</title>
    <link rel="stylesheet" href="/assets/css/user/mypage.css">
</head>
<body>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<div class="header-title" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
    <!--나중에 컴포넌트로 바꿀 것.-->
    <img src="/assets/img/header/header-title.png" alt="헤더 타이틀" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
</div>
<div class="mypage-container">
    <div class="mypage-tabs">
        <button class="tab-btn active" data-tab="info">내 정보</button>
        <button class="tab-btn" data-tab="tickets">예매내역</button>
        <button class="tab-btn" data-tab="points">포인트내역</button>
        <button class="tab-btn" data-tab="fairy">승리요정</button>
    </div>

    <div id="tab-content-container"></div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const tabs = document.querySelectorAll('.mypage-tabs .tab-btn');
        const contentContainer = document.getElementById('tab-content-container');

        // 내 정보 조회, 수정
        function bindInfoForm() {
            const form = document.getElementById("infoForm");
            const editBtn = document.getElementById("editBtn");
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
        function bindTickets() {
            const tryRender = () => {
                const container = document.getElementById('tickets');
                if (!container) {
                    setTimeout(tryRender, 100); // tickets.jsp가 완전히 렌더링될 때까지 기다림
                    return;
                }

                fetch('/user/tickets')
                    .then(res => res.json())
                    .then(tickets => {
                        console.log(">>> 받은 예매 데이터:", tickets);
                        console.log("matchDate:", tickets[0].matchDate);
                        console.log("stadium:", tickets[0].stadium);
                        console.log("seatInfo:", tickets[0].seatInfo);
                        if (!tickets.length) {
                            container.innerHTML = '<p class="no-tickets-msg">예매내역이 없습니다.</p>';
                            return;
                        }

                        container.innerHTML = '';
                        tickets.forEach(ticket => {
                            const item = document.createElement('div');
                            item.classList.add('ticket-item');
                            item.innerHTML = `
                                <div class="ticket-top">
                                    <img src="/assets/img/teamlogos/1.png" class="logo-team">
                                    <span class="vs-text">VS</span>
                                    <img src="/assets/img/teamlogos/10.png" class="logo-team">
                                    <span class="match-date">\${ticket.matchDate}</span>
                                    <img src="/assets/img/mypage/chevron-right.svg" class="arrow-icon">
                                </div>
                                <div class="ticket-info">
                                       <div class="info-row">
                                         <span class="label">예매자</span>
                                         <span class="user">\${ticket.userName}</span>
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
                                        <span class="seat">\${ticket.seatInfo}</span>
                                      </div>
                                </div>
                              `;
                            container.appendChild(item);

                            const arrow = item.querySelector('.arrow-icon');
                            arrow.addEventListener('click', () => {
                                item.classList.toggle('expanded');
                            });
                        });

                    });
            };

            tryRender(); // 실행
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
                    }
                    // else if (tabName==='points') bindPoints();
                    // else if (tabName==='fairy')   bindFairy();
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
