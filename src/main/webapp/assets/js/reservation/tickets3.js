const notifications = document.querySelectorAll('.notification');
const closeButtons = document.querySelectorAll('.closeBtn');
const befortBtn = document.querySelector('#beforeBtn');
const payBtn = document.querySelector('#payBtn');


document.addEventListener('DOMContentLoaded', () => {

    /* 상세보기 오픈 */
    notifications.forEach(noti => {
        noti.addEventListener('click', () => {
            let notiDetail;

            if (noti.id === 'notification2') {
                notiDetail = document.getElementById('noti2');
            } else if(noti.id === 'notification3') {
                notiDetail = document.getElementById('noti3');
            } else if(noti.id === 'notification4') {
                notiDetail = document.getElementById('noti4');
            }

            if(notiDetail) {
                notiDetail.style.display = 'block';
            }
        })
    })

    /* 상세보기 닫기 */
    closeButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const container = btn.closest('.notification-container');
            if(container) {
                container.style.display = 'none';
            }
        })
    })

    befortBtn.addEventListener("click", (e) => {
        window.location.href="/reservation/discount";
    });


    payBtn.addEventListener("click", async (e) => {
        console.log('click?');
        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
        const allChecked = Array.from(checkboxes).every(cb => cb.checked);

        if (!allChecked) {
            alert("모든 항목에 동의하셔야 다음 단계로 진행할 수 있습니다.");
            e.preventDefault();
        } else {
            const priceData = {
                price: document.getElementById('price').innerText.replace(/,/g, ''),
                point: document.getElementById('usedPoint').innerText.replace(/,/g, ''),
                total: document.getElementById('totalPay').innerText.replace(/,/g, ''),
            };

            sessionStorage.setItem('priceInfo', JSON.stringify(priceData));
            console.log(1);
            await requestPay();
        }
    });

    document.getElementById("buyerName").innerText = user.userName;
    document.getElementById("buyerPhone").innerText = user.tel;
    document.getElementById("buyerEmail").innerText = user.email;
    document.getElementById("myPoint").innerText = user.totalPoint + " 원";

    // 경기 정보 채우기
    document.getElementById("opponentTeam").src = `/assets/img/teamlogos/`+ gameData.oppTeamPk + `.png` || "";
    document.getElementById("opponentTeamName").innerText = gameData.oppTeamName || "상대팀";
    document.getElementById("right-date").innerText = gameData.gameDate || "경기일시";

    // 예매 정보 채우기
    document.getElementById("zoneName").innerHTML = seatsData.map(seat => {
        return `${zoneData}`;
    }).join('<br>');

    document.getElementById("seatDetail").innerHTML = seatsData.map(seat => {
        const row = seat.charAt(0);
        const number = seat.substring(1);
        return `${row}열 ${number}석`;
    }).join('<br>');


    // 가격 정보 채우기
    document.getElementById("price").innerText = price.toLocaleString();
    document.getElementById("usedPoint").innerText = point.toLocaleString();
    document.getElementById("totalPay").innerText = (price - point).toLocaleString();
    console.log('2222');
    console.log(document.getElementById("price").innerText);
});

// 아임포트 초기화
IMP.init("imp14397622");  // 자신의 가맹점 식별코드로 바꾸세요

async function requestPay() {
    let priceInfo = JSON.parse(sessionStorage.getItem('priceInfo'));
    console.log(priceInfo);
    let price = priceInfo.total;
    console.log(price);
    IMP.request_pay({
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "order_" + new Date().getTime(),
        name: "티켓 결제",
        amount: parseInt(price), // 결제 금액 (숫자형)
        buyer_email: user.email,
        buyer_name: user.userName,
        buyer_tel: user.tel
    }, function (rsp) {
        if (rsp.success) {
            console.log(rsp);
            // 결제 성공 시 처리 (백엔드에 결제 정보 검증 요청 필요)
            alert("결제 성공\n고유ID: " + rsp.imp_uid + "\n상점 거래ID: " + rsp.merchant_uid);
            // 예: fetch("/payment/verify", { method: "POST", body: JSON.stringify(rsp) })
        } else {
            // 결제 실패
            alert("결제 실패: " + rsp.error_msg);
        }
    });
}
