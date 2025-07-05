import {openLoading, closeLoading} from "./loading.js";

const seats = JSON.parse(localStorage.getItem('seats' + gamePk));
const quantity = seats.length;
const discountInfo = JSON.parse(localStorage.getItem('discountInfo' + gamePk));
const cost = Number(JSON.parse(localStorage.getItem('zone' + gamePk))['cost']);
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
                  <select class="cnt-dropdown" id="discnt-${dis.discountPk}" name="cnt-${dis.discountPk}">
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
        const reservelistPk = Number(localStorage.getItem('reservelistPk' + gamePk));
        const zonePk = Number(JSON.parse(localStorage.getItem('zone' + gamePk))['zonePk']);
        if(confirm(`이전 페이지로 돌아가면 지금까지의 기록이 삭제됩니다. 돌아가시겠습니까?`)) {
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
                        //선점이 되어있으면
                        //컨트롤러에서 선점 여부 확인 후 선점
                        const sendData = {
                            gamePk: gamePk,
                            seats: JSON.parse(localStorage.getItem('seats' + gamePk)),
                            zonePk: Number(zonePk),
                            reservelistPk: Number(reservelistPk)
                        };

                        fetch(`/reservation/preemption/delete`, {
                            method: 'DELETE',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(sendData)
                        })
                            .then(res => {
                                return res.json();
                            })
                            .then(data => {
                                if(data === 2) {
                                    alert("서버 오류");
                                    window.close();
                                } else if(data === 0) {
                                    alert("로그인이 필요합니다.");
                                    removeData();
                                    window.close();
                                } else {
                                    closeLoading();
                                    //예약 번호 세션 스토리지에서 삭제
                                    localStorage.removeItem('reservelistPk' + gamePk);

                                    //선택한 구역 정보 세션 스토리지에서 삭제 (구역 번호, 구역명, 가격, 구역 색상, 좌석 총 개수, 남은 개수)
                                    localStorage.removeItem('seats' + gamePk);

                                    ////선택한 좌석 목록 세션 스토리지에서 삭제
                                    localStorage.removeItem('zone' + gamePk);

                                    location.href = `/reservation/seat?gamePk=${gamePk}`;
                                }
                            })
                            .catch(error => {
                                alert(`서버 오류 발생`);
                                removeData();
                                window.close();
                            });
                    }
                })
                .catch(error => {
                    alert(`서버 오류 발생`);
                    removeData();
                    window.close();
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
            localStorage.setItem('totalPay' + gamePk, totalPayment);

            //선점 확인
            const zonePk = Number(JSON.parse(localStorage.getItem('zone' + gamePk))['zonePk']);

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
                        closeLoading();
                        //선택한 할인 정보 불러오기
                        let discountPk = [];
                        document.querySelectorAll('.cnt-dropdown').forEach(el => {
                            let cnt = el.value; //선택 매수
                            let id = el.id.split('-')[1];

                            for(let i = 0; i < cnt; ++i) {
                                discountPk.push(id);
                            }
                        });

                        //세션에 저장
                        localStorage.setItem('discountPk' + gamePk, JSON.stringify(discountPk));

                        location.href = `/reservation/confirm?gamePk=${gamePk}`;
                    }
                })
                .catch(error => {
                    alert(`서버 오류 발생`);
                    removeData();
                    window.close();
                });


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

function removeData() {
    localStorage.clear();
    navigator.sendBeacon('${pageContext.request.contextPath}/session/clear');
}