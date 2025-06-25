export const standardX = 96.6;
export const standardY = 50;

document.addEventListener("DOMContentLoaded", () => {
    getSeatsMap(1);
})

/* 유형에 맞는 좌석 svg 불러오기 */
function getSeatsMap(type) {
    const svgMap = document.getElementById("svgMap");
    let svgType = '/assets/img/tickets/';
    if (type === 1) svgType += '15-20.svg';
    else if (type === 2) svgType += '20-9.svg';
    else if (type === 3) svgType += '15-8.svg';

    fetch(svgType)
        .then(res => res.text())
        .then(svgText => {
            const parser = new DOMParser();
            const svgDoc = parser.parseFromString(svgText, "image/svg+xml");
            const svgElement = svgDoc.querySelector('svg');

            if (svgElement) {
                svgElement.setAttribute('id', 'svgMap');  // ✅ id 추가
                document.querySelector('.map-wrapper').innerHTML = '';
                document.querySelector('.map-wrapper').appendChild(svgElement);
            }
        });
}