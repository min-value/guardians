//test 용
sessionStorage.setItem("gameInfo", JSON.stringify({
    gamePk: 87,
    oppTeamPk: 1,
    oppTeamName: "한화 Eagles",
    gameDate: "2025-05-26"
}));

sessionStorage.setItem("totalAmount", 110);
sessionStorage.setItem("usedPoint", 0);
sessionStorage.setItem("paidAmount", 110);
sessionStorage.setItem("reservelistPk", 31);


sessionStorage.setItem("zone", JSON.stringify({
    zonePk: 1101,
    zoneName:"1루 외야석",
    zoneColor:"#0DB310",
    cost: 11000,
    totalNum:500,
    remainingNum:500
}));
sessionStorage.setItem("seats", JSON.stringify(["a1", "a2", "a3"]));

const gameInfo = JSON.parse(sessionStorage.getItem('gameInfo'));
const zone = JSON.parse(sessionStorage.getItem('zone'));
const seats = JSON.parse(sessionStorage.getItem('seats'));
const totalAmount = JSON.parse(sessionStorage.getItem('totalAmount'));
const usedPoint = JSON.parse(sessionStorage.getItem('usedPoint'));
const paidAmount = JSON.parse(sessionStorage.getItem('paidAmount'));

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('opponentTeam').setAttribute('src', `/assets/img/teamlogos/${gameInfo['oppTeamPk']}.png`);
    document.getElementById('opponentTeamName').innerText = gameInfo['oppTeamName'];
    document.getElementById('right-date').innerText = formatTimestamp(gameInfo['gameDate']);

    //예매 정보 넣기
    const seatWrapper = document.querySelector('.selectedSeat-wrapper');
    seats.forEach(seat => {
        const seatDiv = document.createElement('div');
        seatDiv.className = 'selectedSeat';

        const zoneDiv = document.createElement('div');
        zoneDiv.className = 'zoneName';
        zoneDiv.textContent = zone['zoneName'];

        const seatDetailDiv = document.createElement('div');
        seatDetailDiv.className = 'seatDetail';
        seatDetailDiv.textContent = seat;
        if(zone['zonePk'] === 1100 || zone['zonePk'] === 1101) {
            seatDetailDiv.style.color = 'red';
        }

        seatDiv.appendChild(zoneDiv);
        seatDiv.appendChild(seatDetailDiv);

        seatWrapper.appendChild(seatDiv);
    });

    //가격 넣기
    document.getElementById('price').innerText = totalAmount || '0';

    //포인트 넣기
    document.getElementById('usedPoint').innerText = usedPoint || '0';

    //결제 금액 넣기
    document.getElementById('totalPay').innerText = paidAmount ||'0';
})

function formatTimestamp(timestamp) {
    const date = new Date(timestamp);

    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 0부터 시작
    const day = date.getDate().toString().padStart(2, '0');

    const weekdays = ['일', '월', '화', '수', '목', '금', '토'];
    const weekday = weekdays[date.getDay()];

    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');

    return `${year}-${month}-${day}(${weekday}) ${hour}:${minute}`;
}