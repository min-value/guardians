const standardX = 130;
const standardY = 50;

let zoomC = document.querySelectorAll('.zoomC')[1];
let zoomP = document.querySelectorAll('.zoomP')[1];

import {zoom} from "./toolbar.js";

let offsetX = 0;
let offsetY = 0;
let startX = 0;
let startY = 0;

document.addEventListener("DOMContentLoaded", () => {
    //드래그 이동
    zoomP.addEventListener('mousedown', mouseDownHandler);
})

/* 좌석 유형 결정 */
export function setSeatType(colorName) {
    if(colorName === '450') {
        return 2;
    } else if(colorName === '500') {
        return 3;
    } else if(colorName === '1100' || colorName === '1101') {
        return 4;
    } else {
        return 1;
    }
}
/* 유형에 맞는 좌석 svg 불러오기 */
export function getSeatsMap(type) {
    let svgType = '/assets/img/reservation/';
    if (type === 1) svgType += '15-20.svg';
    else if (type === 2) svgType += '20-9.svg';
    else if (type === 3) svgType += '15-8.svg';

    document.getElementById('seatObj').data = svgType;
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