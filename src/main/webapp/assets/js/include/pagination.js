function createPagination({
                              currentPage,
                              totalCount,
                              onPageChange,
                              pageSize = 10,
                              containerId = '#pagination',
                          }) {
    const pageCount = Math.ceil(totalCount / pageSize);
    const pagination = $(containerId).empty();

    //◀ 이전 버튼
    const prev = $(`<button class="pageLeft"></button>`);
    prev.prop('disabled', currentPage === 1);
    prev.on('click', () => {
        if (currentPage > 1) onPageChange(currentPage - 1);
    });
    pagination.append(prev);

    //내부 출력
    for (let i = 1; i <= pageCount; i++) {
        if (i === 1 || i === pageCount || Math.abs(i - currentPage) <= 1) {
            const btn = $(`<button class="pageBtn">${i}</button>`);
            if (i === currentPage) btn.addClass('active');
            btn.on('click', () => onPageChange(i));
            pagination.append(btn);
        } else if (i === currentPage - 2 || i === currentPage + 2) {
            pagination.append(`<span class="pageEllipsis"></span>`);
        }
    }

    // ▶ 다음 버튼
    const next = $('<button class="pageRight"></button>');
    next.prop('disabled', currentPage === pageCount);
    next.on('click', () => {
        if (currentPage < pageCount) onPageChange(currentPage + 1);
    })
    pagination.append(next);
}