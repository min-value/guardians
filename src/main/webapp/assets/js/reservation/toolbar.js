import {getSeatsMap, setSeatType} from "./seats.js";
import {openLoading, closeLoading} from "./loading.js";

import {
    changeZoneInfoListBox,
    map,
    scrollToZoneInfo,
    setMap,
    setZoneInfo,
    updateZoneInfoHighlight, zoneInfo
} from "./tickets1.js";

const standardX = 130;
const standardY = 50;

const zoomCs = document.querySelectorAll('.zoomC');
const zoomPs = document.querySelectorAll('.zoomP');

let zoomC;
let zoomP;

export let zoom = 1;



let offsetX = 0;
let offsetY = 0;
let startX = 0;
let startY = 0;

import {
    addSeatListener,
    currentView,
    lastColoredName,
    resetSelectedAll,
    setColored,
    setCurrentView
} from "./tickets1.js";
import {selectedSeats} from "./tickets1.js";
import {colored} from "./tickets1.js";

document.addEventListener("DOMContentLoaded", () => {
    //툴바 버튼 이벤트 리스너 추가 
    const gobackBtn = document.querySelector('#gobackBtn');
    const enlargementBtn = document.querySelector('#enlargementBtn');
    const reductionBtn = document.querySelector('#reductionBtn');
    const reloadBtn = document.querySelector('#reloadBtn');


    const mask = document.getElementById('highlight-mask');
    const overlay = document.getElementById('overlay');

    //zone일 때 (최초 로드) zoomP, zoomC 세팅
    setZoom();

    //색상 회복
    gobackBtn.addEventListener('click', () => {
        if(currentView === 'zone') {
            colorRestore(mask, overlay);
        } else if(currentView === 'seat') {
            switchToZone();
        }
    });
    //확대
    enlargementBtn.addEventListener('click', () => {
        zoomIn();
    })

    //축소
    reductionBtn.addEventListener('click', () => {
        zoomOut();
    })

    //리로드
    reloadBtn.addEventListener('click', () => {
        reload();
    })
})

/* svgMap 내의 region 색상 회복 */
export function colorRestore(mask, overlay) {
    while (mask.children.length > 1) {
        mask.removeChild(mask.lastChild);
    }

    overlay.setAttribute('visibility', 'hidden');

    setColored(0);

    //zoneInfo 포커싱 해제
    scrollToZoneInfo(  null);
    updateZoneInfoHighlight(null);
}

/* zone으로 이동 */
export function switchToZone() {
    setCurrentView(1);
    //선택한 좌석들 초기화: 선택 0
    resetSelectedAll();

    //select 변경
    setZoom();

    //seat 화면 display:none 설정
    document.querySelector('.seats-container').style.display = 'none';
    document.querySelector('.stadium-container').style.display = 'flex';
}
/* 확대 및 축소 */
function zoomIn() {
    if(zoom < 2) {
        zoom += 0.2;
        applyTransform();
    }
}

function zoomOut() {
    if(zoom > 1) {
        zoom -= 0.2;
        applyTransform();
    }
}



/* 드래그 이동 */
const mouseDownHandler = function(e) {
    //누른 마우스 위치 값을 가져와 지정
    startX = e.clientX;
    startY = e.clientY;

    //마우스 이동 및 해제 이벤트 등록
    document.addEventListener('mousemove', mouseMoveHandler);
    document.addEventListener('mouseup', mouseUpHandler);
}

const mouseMoveHandler = function(e) {
    const dx = e.clientX - startX;
    const dy = e.clientY - startY;

    const tempOffsetX = offsetX + dx;
    const tempOffsetY = offsetY + dy;

    const objectInfo = zoomC.getBoundingClientRect();
    const wrapperInfo = zoomP.getBoundingClientRect();

    const topGap = wrapperInfo.top - objectInfo.top;
    const bottomGap = wrapperInfo.bottom - objectInfo.bottom;
    const leftGap = wrapperInfo.left - objectInfo.left;
    const rightGap = wrapperInfo.right - objectInfo.right;

    const outOfBounds =
        topGap < -standardY || bottomGap > standardY ||
        leftGap < -standardX || rightGap > standardX;

    if (outOfBounds) {
        zoomC.style.transform = `translate(${tempOffsetX}px, ${tempOffsetY}px) scale(${zoom})`;
        zoomC.style.transition = 'transform 0s';
    } else {
        offsetX = tempOffsetX;
        offsetY = tempOffsetY;
        applyTransform();
    }

    startX = e.clientX;
    startY = e.clientY;
}


const mouseUpHandler = function() {
    // 범위를 넘어간 경우: 마우스 해제 시, 제한선 안쪽으로 되돌리기
    const objectInfo = zoomC.getBoundingClientRect();
    const wrapperInfo = zoomP.getBoundingClientRect();

    let corrected = false;

    if (wrapperInfo.top - objectInfo.top < -standardY) {
        offsetY += (wrapperInfo.top + standardY) - objectInfo.top;
        corrected = true;
    }
    else if (wrapperInfo.bottom - objectInfo.bottom > standardY) {
        offsetY +=  (wrapperInfo.bottom - standardY) - objectInfo.bottom;
        corrected = true;
    }
    if (wrapperInfo.left - objectInfo.left < -standardX) {
        offsetX += (wrapperInfo.left - standardX) - objectInfo.left;
        corrected = true;
    }
    else if (wrapperInfo.right - objectInfo.right > standardX) {
        offsetX += (wrapperInfo.right - standardX) - objectInfo.right;
        corrected = true;
    }

    if (corrected) {
        zoomC.style.transition = 'transform 0.5s ease';
    }

    applyTransform();

    // 마우스 이벤트 제거
    document.removeEventListener('mousemove', mouseMoveHandler);
    document.removeEventListener('mouseup', mouseUpHandler);
}

function applyTransform() {
    zoomC.style.transition = 'transform 0s';
    zoomC.style.transformOrigin = 'top left';
    zoomC.style.transform = `translate(${offsetX}px, ${offsetY}px) scale(${zoom})`;
}

export function setZoom() {
    zoom = 1;

    if(currentView === 'zone') {
        zoomC = zoomCs[0];
        zoomP = zoomPs[0];
    } else if(currentView === 'seat') {
        zoomC = zoomCs[1];
        zoomP = zoomPs[1];
    }
}

/* 리로드 함수 */
export function reload() {
    openLoading();
    fetch(`/reservation/info?gamePk=${gamePk}`)
        .then(res => res.json())
        .then(data => {
            setMap(data.zoneMapDetail);
            setZoneInfo(data.zoneInfo);
            console.log(map);
            console.log(zoneInfo);
            /* 새로고침 방식 변경 */
            // $('.zoneInfo-wrapper').load(window.location.href + ' .zoneInfo-wrapper', function() {
            //     scrollToZoneInfo(lastColoredName);
            //     updateZoneInfoHighlight(lastColoredName);
            //     document.querySelectorAll('.zoneInfo').forEach(el => {
            //         el.addEventListener('click', () => {
            //             let zonePk = el.id.split('zone')[0];
            //             changeZoneInfoListBox(zonePk);
            //         });
            //     });
            // });
            reloadSelectZone();

            resetSelectedAll();
            getSeatsMap(setSeatType(lastColoredName));
            addSeatListener();

            closeLoading();
        });

}
/* 등급 선택 정보 리로드 */
function reloadSelectZone() {
    document.querySelectorAll(".zoneInfo").forEach(el => {
        let zonePk = el.id.split('zone')[0];
        el.querySelector('.zoneVacancies').innerHTML = zoneInfo[zonePk]['remainingNum'] + '석';
    });
}