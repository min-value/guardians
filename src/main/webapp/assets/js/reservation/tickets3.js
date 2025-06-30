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
            const usedPointStr = document.getElementById('usedPoint').innerText.replace(/,/g, '').replace(/[^0-9]/g, '');
            const usedPointNum = parseInt(usedPointStr, 10);
            sessionStorage.setItem('usedPoint', JSON.stringify(usedPointNum));
            sessionStorage.setItem('paidAmount', JSON.stringify(document.getElementById('totalPay').innerText.replace(/,/g, '')));
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
    let totalAmount = JSON.parse(sessionStorage.getItem('totalAmount'));
    let usedPoint = JSON.parse(sessionStorage.getItem('usedPoint'));
    let paidAmount = JSON.parse(sessionStorage.getItem('paidAmount'));

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
                        reservelist_pk: JSON.parse(sessionStorage.getItem("reservelistPk")),
                        impUid: res.imp_uid,
                        used_point: usedPoint,
                        total_amount: totalAmount,
                        paid_amount: res.paid_amount,
                        apply_num: res.apply_num,
                        card_code: res.card_code,
                        card_name: res.card_name,
                        card_number: res.card_number,
                        user_pk: user.userPk
                    }),
                    success: function(result) {
                        console.log("서버 응답:", result);
                        if (result === true) {
                            alert("예매 성공!");
                            window.location.href = "/tickets/all?showModal=true";
                        } else {
                            alert("예매 저장 실패. 다시 시도해주세요.");
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
