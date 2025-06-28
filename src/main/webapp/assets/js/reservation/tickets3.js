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


    payBtn.addEventListener("click", (e) => {
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
    document.getElementById("price").innerText = '11111';//price.toLocaleString();
    document.getElementById("usedPoint").innerText = point.toLocaleString();
    document.getElementById("totalPay").innerText = (price - point).toLocaleString();
    console.log('2222');
    console.log(document.getElementById("price").innerText);
});

