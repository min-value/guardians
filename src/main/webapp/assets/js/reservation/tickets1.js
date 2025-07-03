import {colorRestore, reload, setZoom} from "./toolbar.js";
import {getSeatsMap, setSeatType} from "./seats.js";
import {openLoading, closeLoading} from "./loading.js";
export let selectedSeats = [];
export let currentView = 'zone'; //zone, seat
export let colored = 0; //0: region 포커스 x, 1: region 포커스 o
export let lastColoredName = null;

let seatSelectMap = {}; // key: seatId, value: true/false

document.addEventListener('DOMContentLoaded', () => {
    const tooltip = document.getElementById('tooltip');

    //선택한 좌석 바 관련 리스너 추가
    document.querySelector('.selectedList-info-container').addEventListener('click', () => {
        showSelectedSeats(selectedSeats.length);
    })

    //currentView가 zone일 때 svgMap에 관한 리스너 추가
    document.querySelectorAll('.region').forEach(region => {
        //zone 클릭 시
        region.addEventListener('click', () => {
            clickZone(region);
        });
        //zone에 커서 올리면 커서 모양 바꾸기
        region.addEventListener('mouseover', () => {
            region.style.cursor = 'pointer';
        });

        //zone 호버
        region.addEventListener("mouseover", (e) => {
            tooltip.innerText = region.id;
            tooltip.style.left = e.clientX + "px";
            tooltip.style.top = e.clientY + "px";
            tooltip.style.opacity = 1;
        });

        region.addEventListener('mouseleave', () => {
            tooltip.style.opacity = 0;
        });
    });

    //다음 단계 벼튼 리스너 추가
    //선택된 좌석이 없다면 다음 단계로 넘어가지 못함
    document.getElementById('ticket-btn').addEventListener('dblclick', function(e) {
        e.preventDefault();
    });

    document.getElementById('ticket-btn' ).addEventListener('click', () => {
        console.log(selectedSeats);
        if(selectedSeats.length === 0) {
            alert("선택된 좌석이 없습니다. 좌석을 선택하세요");
            return;
        }

        //컨트롤러에서 선점 여부 확인 후 선점
        const sendData = {
            quantity: Number(selectedSeats.length),
            gamePk: gamePk,
            zonePk: Number(lastColoredName),
            seats: selectedSeats
        };

        //다음 단계 오버레이 지우기
        let popup = document.querySelector('.autoAssigned');
        let overlay = document.querySelector('.overlay');

        popup.style.display = 'none';
        overlay.style.display = 'none';

        //로딩 창 띄우기
        openLoading();
        fetch('/reservation/preemption/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sendData)
        })
            .then(res => {
                return res.json();
            })
            .then(data => {
                closeLoading();
                if(data.preempted === 1) {
                    //예약 번호 세션 스토리지에 저장
                    localStorage.setItem('reservelistPk' + gamePk, JSON.stringify(data.reservelistPk));

                    //선택한 구역 정보 세션 스토리지에 저장 (구역 번호, 구역명, 가격, 구역 색상, 좌석 총 개수, 남은 개수)
                    localStorage.setItem('zone' + gamePk, JSON.stringify(zoneInfo[lastColoredName]));

                    //선택한 좌석 목록 세션 스토리지에 저장
                    localStorage.setItem('seats' + gamePk, JSON.stringify(selectedSeats));

                    location.href = `/reservation/discount?gamePk=${gamePk}`;
                } else if(data.preempted === 0) {
                    alert(`${data.errorMsg}`);
                    location.reload();
                } else if(data.preempted === 2) {
                    alert(`${data.errorMsg}`);
                    removeData();
                    window.close();
                }
            })
            .catch(error => {
                alert(`서버 오류 발생`);
                removeData();
                window.close();
            });

    });

    //리셋 버튼 리스너 추가
    document.getElementById('resetBtn').addEventListener('click', () => {
        resetSelectedAll();
    });

    //등급 선택 옆 새로고침 버튼 리스너 추가
    document.getElementById('zoneReloadBtn').addEventListener('click', () => {
        reload();
    })

    //등급 선택 클릭 리스너 추가: 클릭 시 좌측 경기장 색상 변경
    document.querySelectorAll('.zoneInfo').forEach(el => {
        el.addEventListener('click', () => {
            let zonePk = el.id.split('zone')[0];
            changeZoneInfoListBox(zonePk);
        })
    })


    //좌석 자동 배정 리스너 추가
    document.querySelectorAll('.changeBtn').forEach(el => {
        if(el.id === 'up') {
            el.addEventListener('click', () => {
                calCount(1);
            });
        } else if(el.id === 'down') {
            el.addEventListener('click', () => {
                calCount(2);
            })
        }
    })
})

/* 좌석 자동 배정 리스너 함수 */
function calCount(type) {
    let cntEl = document.getElementById('count');
    let cnt = Number(cntEl.value);
    if(type === 1) {
        //+
        if(cnt >= available) {
            alert('최대 예매 가능 매수를 초과하였습니다.');
        } else {
            cntEl.value = cnt + 1;
            selectedSeats.push('자동배정');
        }
    } else if(type === 2) {
        //-
        if(cnt > 0) {
            cntEl.value = cnt - 1;
            selectedSeats.pop();
        }
    }
    console.log(selectedSeats)
}
/* 등급 선택 클릭 리스너 */
function changeZoneInfoListBox(zonePk) {
    //svgMap에서 해당 존 가져오기
    let region = document.getElementById(zonePk);
    const mask = document.getElementById('highlight-mask');
    const overlay = document.getElementById('overlay');

    if(colored === 1 && zonePk === lastColoredName) {
        //포커스 O
        updateZoneInfoHighlight(null);
        colorRestore(mask, overlay);
    } else {
        //포커스 X
        updateZoneInfoHighlight(zonePk);
        changeColor(region);
    }
}
/* 특정 zone 선택 시 해당 zonePk를 아이디로 가지고 있는 요소를 등급 선택존에서 포커스 */
export function scrollToZoneInfo(zonePk) {
    let targetId = zonePk + 'zone';

    const container = document.querySelector('.zoneInfo-listBox');
    const target = document.getElementById(targetId);

    if(zonePk === null) {
        const firstChild = document.querySelector('.zoneInfo-listBox').firstChild;
        container.scrollTo({
            top: firstChild.offsetTop - 340,
            behavior: 'smooth'
        });
    } else {
        container.scrollTo({
            top: target.offsetTop - 340,
            behavior: 'smooth'
        });
    }
}

/* 특정 존 선택 시 해당 zonePk를 아이디로 가지고 있는 요소의 색깔을 변경 */
export function updateZoneInfoHighlight(currentId) {
    // 이전에 선택된 zoneInfo에서 클래스 제거
    const prevZoneDiv = document.getElementById(lastColoredName + 'zone');
    if (prevZoneDiv) {
        prevZoneDiv.classList.remove('highlight-zone');
    }

    if(currentId !== null) {
        // 현재 선택된 zoneInfo에 클래스 추가
        const currentZoneDiv = document.getElementById(currentId + 'zone');
        if (currentZoneDiv) {
            currentZoneDiv.classList.add('highlight-zone');
        }
    }
}

/* 선택한 좌석들 표시하는 토글 */
function showSelectedSeats() {
    const container = document.querySelector('.selectedList-comp-container')
    const dropdownImg = document.querySelector('#selectedList-dropdown');

    const isShown = container.classList.toggle('show');

    if(isShown) {
        //드랍 다운이 열린 경우
        dropdownImg.src = "/assets/img/reservation/dropDownWhiteDown.svg";
    } else {
        dropdownImg.src = "/assets/img/reservation/dropDownWhiteUp.svg";
    }
}

/* zone 클릭 시 이벤트 */
function clickZone(region) {
    if(colored === 1 && lastColoredName === region.id) {
        switchToSeat();
        setZoom();
    } else {
        updateZoneInfoHighlight(region.id);
        changeColor(region);
        scrollToZoneInfo(region.id);
    }
}

/* svgMap 내의 region 색상 변경 */
function changeColor(region) {
    const mask = document.getElementById('highlight-mask');
    const overlay = document.getElementById('overlay');
    while (mask.children.length > 1) {
        mask.removeChild(mask.lastChild);
    }

    const hole = region.cloneNode(true);
    hole.setAttribute('fill', 'black');
    mask.appendChild(hole);

    overlay.setAttribute('visibility', 'visible');

    colored = 1;
    lastColoredName = region.id;
}


/* seat으로 이동 */
function switchToSeat() {
    let seatType = setSeatType(lastColoredName);

    if(seatType === 4) {
        //외야석 자동 배정
        autoAssigned();
        return;
    } else {
        setCurrentView(2);
        getSeatsMap(seatType);
    }

    document.querySelector('.stadium-container').style.display = 'none';
    document.querySelector('.seats-container').style.display = 'flex';

    addSeatListener();
}

export function addSeatListener() {
    //세션에 저장되어 있는 값에 따라 색깔 칠하기
    let seatObject = document.getElementById('seatObj');
    let zoneDetail = map[lastColoredName];

    seatObject.addEventListener('load', function() {
        const seatDoc = seatObject.contentDocument;
        const seatElement = seatDoc.querySelectorAll('.seat');

        //seat에 커서 올리면 커서 모양 바꾸기
        seatElement.forEach(el => {
            if(zoneDetail.includes(el.id)) {
                //이미 팔린 좌석인 경우
                el.style.fill = '#E1E1E1';
                el.style.pointerEvents = 'none';
            } else {
                //예매 가능한 좌석인 경우

                //색상 변경
                el.style.fill = '#B748E7';
                //마우스 포인터 변경
                el.addEventListener('mouseover', () => {
                    el.style.cursor = 'pointer';
                });


                //클릭 시: 선택된 좌석에 추가 및 모양 변경
                el.addEventListener('click', () => {
                    if(seatSelectMap[el.id] === true) {
                        for(let i = 0; i < selectedSeats.length; ++i) {
                            if(selectedSeats[i] === el.id) {
                                resetSelectedSeat(el, i);
                                break;
                            }
                        }
                    } else {
                        if(selectedSeats.length < available) {
                            el.setAttribute('rx', 10);
                            el.setAttribute('ry', 10);
                            el.setAttribute('stroke', 'black');
                            el.setAttribute('stroke-width', '3');

                            selectedSeats.push(el.id);
                            document.getElementById('selectList-num').innerHTML = selectedSeats.length + '';
                            document.querySelector(".selectedList-comp-wrapper")
                                .innerHTML += `
                            <div class="selectedSeatComp-container" id="${el.id}">
                                <div class="selectedSeatComp-wrapper">
                                    <div class="selectedSeatComp-zone-color-wrapper">
                                        <div class="selectedSeatComp-zone-color" style="background-color: ${zoneInfo[lastColoredName].zoneColor}">
                                        </div>
                                    </div>
                                    <div class="selectedSeatComp-zone-name-wrapper">
                                        <div class="selectedSeatComp-zone-name">
                                            ${zoneInfo[lastColoredName].zoneName}
                                        </div>
                                    </div>
                                    <div class="selectedSeatComp-zone-detail-wrapper">
                                        <div class="selectedSeatComp-zone-detail">
                                            ${el.id}
                                        </div>
                                    </div>
                                </div>
                            </div>`;
                            seatSelectMap[el.id] = true;
                        } else {
                            alert(`최대 예매 가능 매수를 초과하였습니다.`);
                        }
                    }
                })
            }
        });
    })
}
export function setCurrentView(num) {
    if(num === 1) {
        currentView = 'zone';
    } else if(num === 2) {
        currentView = 'seat';
    }
}

//지정한 좌석 선택 해제
export function resetSelectedSeat(seat, idx) {
    seat.setAttribute('rx', 0);
    seat.setAttribute('ry', 0);
    seat.setAttribute('stroke', 'none');

    selectedSeats.splice(idx, 1);

    document.getElementById('selectList-num').innerHTML = selectedSeats.length + '';
    document.getElementById(seat.id).remove();

    seatSelectMap[seat.id] = false;
}

export function resetSelectedAll() {
    let seatObject = document.getElementById('seatObj');
    const seatDoc = seatObject.contentDocument;

    for (let i = selectedSeats.length - 1; i >= 0; --i) {
        const seatId = selectedSeats[i];
        resetSelectedSeat(seatDoc.getElementById(seatId), i);
    }
}

export function setColored(num) {
    colored = num;
}

/* 외야석 자동 배정 함수 */
function autoAssigned() {
    let popup = document.querySelector('.autoAssigned');
    let overlay = document.querySelector('.overlay');
    const ticketBtn = document.getElementById('ticket-btn');
    //팝업 보이기
    document.getElementById('autoAssigned-choiceInfo').innerText = zoneInfo[lastColoredName]['zoneName'];
    document.getElementById('remainingSeats').innerText = zoneInfo[lastColoredName]['remainingNum'] + '석';
    popup.style.display = 'block';
    overlay.style.display = 'flex';

    //다른 요소들 안 눌리게 변경
    function outsideClickHandler(e) {
        if (!popup.contains(e.target) && e.target !== ticketBtn) {
            closePopup(popup, overlay);
            document.removeEventListener('mouseup', outsideClickHandler);
        }
    }

    document.querySelector('.overlay').addEventListener('mouseup', outsideClickHandler);

    // 닫기 버튼
    document.querySelector('.autoAssigned-closeBtn > img').addEventListener('click', () => {
        closePopup(popup, overlay);
        document.removeEventListener('mouseup', outsideClickHandler);
    });
}

function closePopup(popup, overlay) {
    document.getElementById('count').value = '0';
    popup.style.display = 'none';
    overlay.style.display = 'none';

    for(let i = selectedSeats.length - 1; i >= 0; --i) {
        selectedSeats.pop();
    }
}


function removeData() {
    localStorage.clear();
    navigator.sendBeacon('${pageContext.request.contextPath}/session/clear');
}