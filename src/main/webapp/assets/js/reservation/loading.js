//로딩 띄우기
export function openLoading() {
    document.querySelector('.loader').style.display = 'block';
    document.querySelector('.loading-overlay').style.display = 'flex';
}

//로딩 닫기
export function closeLoading() {
    document.querySelector('.loader').style.display = 'none';
    document.querySelector('.loading-overlay').style.display = 'none';
}