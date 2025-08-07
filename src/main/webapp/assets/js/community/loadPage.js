let currentType = null;
let currentKeyword = null;

function loadPage(page){
    const params = new URLSearchParams(window.location.search);
    const searchType = params.get('type');
    const searchKeyword = params.get('keyword');

    let type = null;
    let keyword = null;

    if (searchKeyword && searchKeyword.trim() !== '') {
        type = searchType;
        keyword = searchKeyword;
        updateButtonStyles('all'); // 검색 시 버튼 상태 초기화
    } else {
        type = currentType;
        keyword = currentKeyword;
    }

    $.ajax({
        url:'/community/page',
        method: 'GET',
        data: {
            page: page,
            type : type,
            keyword: keyword
        },
        success: function(res){
            const list = res.list;
            const totalCount = res.totalCount;

            const container = $('#post-container').empty();
            list.forEach(post => {
                const date = new Date(post.p_date);
                const formattedDate = `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2,'0')}-${date.getDate().toString().padStart(2,'0')} ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`;
                container.append(`
                        <div id="post">
                            <div class="title">
                                <a href="/community/post/${post.post_pk}" class="post-link">${post.title}</a>
                            </div>
                            <div class="writer">${post.user_name}</div>
                            <div class="date">${formattedDate}</div>
                        </div>`
                );
            });

            if (totalCount > 0) {
                createPagination({
                    currentPage: page,
                    totalCount: totalCount,
                    onPageChange: (newPage) => loadPage(newPage),
                    pageSize: 10,
                    containerId: '#pagination'
                });
            }
            else{
                const container2 = $('#pagination').empty();
                container2.append(``);
                container.append(`<div class="no-data">
                        작성된 글이 없습니다.
                    </div>`);
            }
        }
    });
}

function updateButtonStyles(active) {
    if (active === 'my') {
        $('#my-posts-btn').css('color', 'black');
        $('#all-posts-btn').css('color', 'var(--gray-03)');
    } else {
        $('#all-posts-btn').css('color', 'black');
        $('#my-posts-btn').css('color', 'var(--gray-03)');
    }
}

$(document).ready(function(){
    $('#all-posts-btn').on('click', function(){
        currentType = null;
        currentKeyword = null;
        updateButtonStyles('all');
        history.pushState(null, null, '/community/post'); // URL 초기화
        loadPage(1);
    });
    $('#my-posts-btn').on('click', function(){
        currentType = 'mine';
        currentKeyword = loginUserPk;
        updateButtonStyles('my');
        history.pushState(null, null, '/community/post'); // URL 초기화
        loadPage(1);
    });

    loadPage(1);
});

