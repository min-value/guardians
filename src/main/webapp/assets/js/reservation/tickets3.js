const notifications = document.querySelectorAll('.notification');
const closeButtons = document.querySelectorAll('.closeBtn');

document.addEventListener('DOMContentLoaded', () => {

    /* 상세보기 오픈 */
    notifications.forEach(noti => {
        noti.addEventListener('click', () => {
            let notiDetail;

            if (noti.id === 'notification2') {
                notiDetail = document.getElementById('noti2');
            } else if(noti.id === 'notification3') {
                notiDetail = document.getElementById('noti3');
            } else if(noti.id === 'notification4') {
                notiDetail = document.getElementById('noti4');
            }

            if(notiDetail) {
                notiDetail.style.display = 'block';
            }
        })
    })

    /* 상세보기 닫기 */
    closeButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const container = btn.closest('.notification-container');
            if(container) {
                container.style.display = 'none';
            }
        })
    })
})