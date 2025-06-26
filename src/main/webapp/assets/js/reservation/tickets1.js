import {setZoom} from "./toolbar.js";
import {getSeatsMap, setSeatType} from "./seats.js";

export let selectedSeats = [];
export let currentView = 'zone'; //zone, seat
export let colored = 0; //0: region 포커스 x, 1: region 포커스 o
export let lastColoredName = null;


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
    document.getElementById('ticket-btn' ).addEventListener('click', () => {
        if(selectedSeats.length === 0) {
            alert("선택된 좌석이 없습니다. 좌석을 선택하세요");
            return;
        }
        location.href = '/reservation/discount';
    });

    document.getElementById('resetBtn').addEventListener('click', () => {
        resetSelectedSeats();
    })
})

/* 선택한 좌석들 표시하는 토글 */
function showSelectedSeats(length) {
    if(length === 0 ) {
        return;
    }
    document.querySelector('.selectedList-comp-container').classList.toggle('show');
}

/* zone 클릭 시 이벤트 */
function clickZone(region) {
    if(colored === 1 && lastColoredName === region.id) {
        switchToSeat();
        setZoom();
    } else {
        changeColor(region);
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
    setCurrentView(2);
    getSeatsMap(setSeatType(lastColoredName));

    document.querySelector('.stadium-container').style.display = 'none';
    document.querySelector('.seats-container').style.display = 'flex';
    //세션에 저장되어 있는 값에 따라 색깔 칠하기
    let seatObject = document.getElementById('seatObj');
    let zoneDetail = map[lastColoredName];

    console.log(zoneDetail);
    seatObject.addEventListener('load', function() {
        const seatDoc = seatObject.contentDocument;
        const seatElement = seatDoc.querySelectorAll('.seat');

        //seat에 커서 올리면 커서 모양 바꾸기
        seatElement.forEach(el => {
            console.log(el.id);
            if(zoneDetail.includes(el.id)) {
                //이미 팔린 좌석인 경우
                console.log(el.id);
                el.style.fill = '#E1E1E1';
                el.style.pointerEvents = 'none';
            } else {
                //예매 가능한 좌석인 경우
                el.addEventListener('mouseover', () => {
                    el.style.cursor = 'pointer';
                });
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

export function resetSelectedSeats() {
    selectedSeats = [];
    //임포트 요소들 삭제
    let selectedListCompWrapper = document.querySelectorAll('selectedList-comp-wrapper');
    selectedListCompWrapper.forEach(wrapper => {
        while(wrapper.firstChild) {
            wrapper.removeChild(wrapper.firstChild);
        }
    })
}

export function setColored(num) {
    colored = num;
}