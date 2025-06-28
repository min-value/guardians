const seats = JSON.parse(sessionStorage.getItem('seats'));
const quantity = seats.length;

document.addEventListener('DOMContentLoaded', () => {
    const selects = document.querySelectorAll('.cnt-dropdown');
    //사용자가 선택한 좌석 개수만큼 옵션 띄우기
    selects.forEach(el => {
        for(let i = 1; i <= quantity; ++i) {
            const option = document.createElement('option');
            option.value = i + '';
            option.text = `${i}매`;
            el.appendChild(option);
        }

        el.addEventListener('click', () => {
            const totalSelected = Array.from(selects).reduce((sum, s) => {
                return sum + parseInt(s.value, 10);
            }, 0);

            if(totalSelected > quantity) {
                alert(`선택한 좌석 수(${quantity}매)를 초과할 수 없습니다.`);
                el.value = '0';
            }
        })
    });

    //이전 단계 버튼 리스너 추가
    document.querySelector('#backBtn').addEventListener('dblclick', function(e) {
        e.preventDefault();
    });

    document.querySelector('#backBtn').addEventListener('click', () => {
        const reservelistPk = Number(sessionStorage.getItem('reservelistPk'));
        const gamePk = Number(JSON.parse(sessionStorage.getItem('gameInfo'))['gamePk']);
        if(confirm(`이전 페이지로 돌아가면 지금까지의 기록이 삭제됩니다. 돌아가시겠습니까?`)) {
            fetch(`/reservation/preemption/delete?reservelistPk=${reservelistPk}`, {
                method: 'DELETE',
            })
                .then(res => {
                    return res.json();
                })
                .then(data => {
                    if(data === false) {
                        throw new Error('삭제 실패');
                    }
                    sessionStorage.removeItem('seats');
                    sessionStorage.removeItem('zone');

                    location.href = `/reservation/seat?gamePk=${gamePk}`;
                })
                .catch(error => {
                    alert(`서버 오류 발생`);
                });
        }
    });

    //다음 단계 버튼 리스너 추가
    document.querySelector('#nextBtn').addEventListener('dblclick', function(e) {
        e.preventDefault();
    })
    document.querySelector('#nextBtn').addEventListener('click', () => {
       location.href = '/reservation/confirm';
    });
})