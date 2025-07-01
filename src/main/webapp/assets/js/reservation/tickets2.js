const seats = JSON.parse(sessionStorage.getItem('seats'));
const quantity = seats.length;
const discountInfo = JSON.parse(sessionStorage.getItem('discountInfo'));
const cost = Number(JSON.parse(sessionStorage.getItem('zone'))['cost']);
let totalSelected = 0;
let totalPayment = 0;

document.addEventListener('DOMContentLoaded', () => {
    //가격 띄우기
    const optionWrapper = document.querySelector('.ticket-options-wrapper');
    discountInfo.forEach(dis => {
        let price = 0;
        if(dis.disRate === 0) {
            price = cost;
        } else {
            price = cost - cost * dis.disRate;
        }

        const html = `
            <div class="ticket-option-wrapper">
              <div class="option-text-wrapper">
                <div class="option-text" id="disname-${dis.discountPk}">${dis.type}</div>
              </div>
              <div class="option-price-wrapper">
                <div class="option-price" id="disprice-${dis.discountPk}">${price}원</div>
              </div>
              <div class="option-cnt-wrapper">
                <div class="option-cnt">
                  <select class="cnt-dropdown" id="discnt-${dis.discountPk}" name="cnt-${dis.id}">
                    <option value="0" selected>0매</option>
                  </select>
                </div>
              </div>
            </div>
          `;
        optionWrapper.insertAdjacentHTML('beforeend', html);
    })

    const selects = document.querySelectorAll('.cnt-dropdown');
    //사용자가 선택한 좌석 개수만큼 옵션 띄우기
    selects.forEach(el => {
        for(let i = 1; i <= quantity; ++i) {
            const option = document.createElement('option');
            option.value = i + '';
            option.text = `${i}매`;
            el.appendChild(option);
        }
    });

    document.querySelectorAll('.cnt-dropdown').forEach(select => {
        select.addEventListener('change', () => {
            totalSelected = Array.from(selects).reduce((sum, s) => {
                return sum + parseInt(s.value, 10);
            }, 0);

            if (totalSelected > quantity) {
                alert(`선택한 좌석 수(${quantity}매)를 초과할 수 없습니다.`);
                select.value = '0';
                return;
            }

            updateTotalPay();
        });
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
        console.log(totalPayment);
        console.log(totalSelected);
        if(totalSelected !== quantity) {
            alert('매수를 정확히 선택해주세요.')
        } else {
            sessionStorage.setItem('totalPay', totalPayment);
            location.href = '/reservation/confirm';
        }
    });
});

/* 총합 계산 */
function updateTotalPay() {
    let totalPay = 0;

    document.querySelectorAll('.cnt-dropdown').forEach(select => {
        const count = parseInt(select.value, 10);

        //가격 추출
        const priceDiv = select.closest('.ticket-option-wrapper').querySelector('.option-price');
        const price = Number(priceDiv.innerHTML.split('원')[0]);

        totalPay += count * price;
    });

    totalPayment = totalPay;
    document.querySelector('#price').innerText = totalPay + '';
    document.querySelector('#totalPay').innerText = totalPay + '';
}