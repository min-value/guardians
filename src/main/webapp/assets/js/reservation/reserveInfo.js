const zone = JSON.parse(localStorage.getItem('zone' + gamePk));
const seats = JSON.parse(localStorage.getItem('seats'+ gamePk));
const totalAmount = JSON.parse(localStorage.getItem('totalPay' + gamePk));
const usedPoint = JSON.parse(localStorage.getItem('usedPoint' + gamePk));
const paidAmount = JSON.parse(localStorage.getItem('paidAmount' + gamePk));

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('opponentTeam').setAttribute('src', `/assets/img/teamlogos/${gameInfo['oppTeamPk']}.svg`);
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
    document.getElementById('totalPay').innerText = paidAmount ||totalAmount;
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