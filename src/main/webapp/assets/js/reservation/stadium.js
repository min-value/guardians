export const standardX = 130;
export const standardY = 50;

document.addEventListener('DOMContentLoaded', () => {
    //svgMap에 대한 이벤트 리스너 추가
    const regions = document.querySelectorAll('.region');
    const mask = document.getElementById('highlight-mask');
    const overlay = document.getElementById('overlay');

    //색상 포커스
    regions.forEach(region => {
        region.addEventListener('click', () => {
            changeColor(mask, region, overlay);
        });
        region.addEventListener('mouseover', () => {
            region.style.cursor = 'pointer';
        });
    });

    //선택된 좌석 관련 리스너 추가
    const infoContainer = document.querySelector('.selectedList-info-container');
    const compContainer = document.querySelector('.selectedList-comp-container');

    infoContainer.addEventListener('click', function() {
        compContainer.classList.toggle('show');
    })
});

/* svgMap 내의 region 색상 변경 */
function changeColor(mask, region, overlay) {
    while (mask.children.length > 1) {
        mask.removeChild(mask.lastChild);
    }

    const hole = region.cloneNode(true);
    hole.setAttribute('fill', 'black');
    mask.appendChild(hole);

    overlay.setAttribute('visibility', 'visible');
}