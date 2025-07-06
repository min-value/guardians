import {openLoading, closeLoading} from "./loading.js";

const notifications = document.querySelectorAll('.notification');
const closeButtons = document.querySelectorAll('.closeBtn');
const beforeBtn = document.querySelector('#beforeBtn');
const payBtn = document.querySelector('#payBtn');

const gameInfo = JSON.parse(localStorage.getItem('gameInfo' + gamePk));
const zone = JSON.parse(localStorage.getItem('zone' + gamePk));
const seats = JSON.parse(localStorage.getItem('seats' + gamePk));
let discountPk = JSON.parse(localStorage.getItem('discountPk' + gamePk)) || ["3"];
discountPk = discountPk.map(Number);
const reservelistPk = JSON.parse(localStorage.getItem('reservelistPk' + gamePk));
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

    beforeBtn.addEventListener("click", (e) => {
        const reservelistPk = Number(localStorage.getItem('reservelistPk' + gamePk));
        const zonePk = Number(JSON.parse(localStorage.getItem('zone' + gamePk))['zonePk']);

        //선점 여부 확인
        const sendConfirm = {
            gamePk: gamePk,
            seats: JSON.parse(localStorage.getItem('seats' + gamePk)),
            zonePk: zonePk
        }

        openLoading();
        fetch(`/reservation/preemption/confirm`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sendConfirm)
        })
            .then(res => {
                return res.json();
            })
            .then(data => {
                if(data === 2) {
                    alert("선점 시간이 만료되었습니다. 다시 시도해주세요.");
                    removeData();
                    window.close();
                } else if(data === 0) {
                    alert("로그인이 필요합니다.");
                    removeData();
                    window.close();
                } else {
                    //선점이 되어있으면 돌아가기
                    window.location.href = `/reservation/discount?gamePk=${gamePk}`;
                }
            })
            .catch(error => {
                alert(`서버 오류 발생`);
                removeData();
                window.close();
            });


    });


    payBtn.addEventListener("click", async (e) => {
        console.log('click?');
        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
        const allChecked = Array.from(checkboxes).every(cb => cb.checked);

        if (!allChecked) {
            alert("모든 항목에 동의하셔야 다음 단계로 진행할 수 있습니다.");
            e.preventDefault();
        } else {
            const usedPointStr = document.getElementById('usedPoint').innerText.replace(/,/g, '').replace(/[^0-9]/g, '');
            const usedPointNum = parseInt(usedPointStr, 10);
            localStorage.setItem('usedPoint' + gamePk, JSON.stringify(usedPointNum));
            localStorage.setItem('paidAmount' + gamePk, JSON.stringify(document.getElementById('totalPay').innerText.replace(/,/g, '')));
            await requestPay();

        }
    });

    document.getElementById("buyerName").innerText = user.userName;
    document.getElementById("buyerPhone").innerText = user.tel;
    document.getElementById("buyerEmail").innerText = user.email;
    document.getElementById("myPoint").innerText = user.totalPoint + " 원";

});

// 아임포트 초기화
IMP.init("imp56774166");  // 자신의 가맹점 식별코드로 바꾸세요

async function requestPay() {
    let totalAmount = JSON.parse(localStorage.getItem('totalPay' + gamePk));
    let usedPoint = JSON.parse(localStorage.getItem('usedPoint' + gamePk));
    let paidAmount = JSON.parse(localStorage.getItem('paidAmount' + gamePk));

    IMP.request_pay({
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "order_" + new Date().getTime(),
        name: "티켓 결제",
        amount: parseInt(paidAmount),
        buyer_email: user.email,
        buyer_name: user.userName,
        buyer_tel: user.tel
    }, function(res) {
        console.log("@@@RES: ", res);
        if (res.success) {
            console.log("성공 imp_uid:", res.imp_uid);
        } else {
            console.error("실패 메시지:", res.error_msg);
        }

        // 결제검증
        $.ajax({
            type : "POST",
            url : "/verifyPayment/" + res.imp_uid
        }).done(function(data) {
            if(res.paid_amount === data.response.amount){
                alert("결제 및 결제검증완료");
                $.ajax({
                    type : "POST",
                    url: "/tickets/purchase",
                    contentType: "application/json",
                    data: JSON.stringify({
                        reservelist_pk: reservelistPk,
                        impUid: res.imp_uid,
                        used_point: usedPoint,
                        total_amount: totalAmount,
                        paid_amount: res.paid_amount,
                        apply_num: res.apply_num,
                        card_code: res.card_code,
                        card_name: res.card_name,
                        card_number: res.card_number,
                        user_pk: user.userPk,
                        game_pk: gameInfo.gamePk,
                        zone_pk: zone.zonePk,
                        seats: seats,
                        discount_pk: discountPk
                    }),
                    success: function(result) {
                        console.log("서버 응답:", result);
                        if (result === true || result === "true") {
                            alert("예매 성공!");
                            if (window.opener && !window.opener.closed) {
                                window.opener.location.href = "/tickets/all?showModal=true";
                            }
                            window.close();
                            console.log(result);
                        } else {
                            alert("예매 저장 실패. 다시 시도해주세요.");
                            $.ajax({
                                type: "POST",
                                url: "/cancelPayment/" + res.imp_uid,
                                success: function (response) {
                                    alert("결제 취소 완료");
                                },
                                error: function (error) {
                                    alert("결제 취소 실패");
                                    console.error(error);
                                }
                            });
                        }
                    },
                    error: function(err) {
                        alert("예매 처리 중 오류 발생");
                        console.error(err);
                    }
                })
            } else {
                alert("결제 실패: ", res.error_msg);
            }
        });
    });
}

function removeData() {
    localStorage.clear();
    navigator.sendBeacon('${pageContext.request.contextPath}/session/clear');
}