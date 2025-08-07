let currentType = null;
let currentKeyword = null;

function loadNews(page){
    const params = new URLSearchParams(window.location.search);
    const searchType = params.get('type');
    const searchKeyword = params.get('keyword');

    let type = null;
    let keyword = null;

    if(searchKeyword && searchKeyword.trim() !== ''){
        type = searchType;
        keyword = searchKeyword;
    }else{
        type = currentType;
        keyword = currentKeyword;
    }

    $.ajax({
        url: '/story/news/page',
        method: 'GET',
        data: {
            page: page,
            type: type,
            keyword: keyword
        },
        success: function (res){
            const list = res.list;
            const totalCount = res.totalCount;

            const container = $('#news-list').empty();
            list.forEach(news => {
                const dateObj = new Date(news.n_writeDate);
                const year = dateObj.getFullYear();
                const month = (dateObj.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작
                const day = dateObj.getDate().toString().padStart(2, '0');
                const hour = dateObj.getHours().toString().padStart(2, '0');
                const minute = dateObj.getMinutes().toString().padStart(2, '0');


                const date = `${year}-${month}-${day} ${hour}:${minute}`;
                container.append(`

                    <div class="news">
                        <div class="news-title" onclick="window.open('${news.news_url}');">
                            ${news.n_title}
                        </div>
                        <div class="news-body">
                            <div class="img" style="background: url('${news.img_url}') no-repeat center center"
                                onclick="window.open('${news.news_url}');"></div>
                            <div class="news-text" onclick="window.open('${news.news_url}');">${news.n_content}</div>
                        </div>
                        <div class="news-date">${date}</div>
                    </div>
                `);
            });

            if(totalCount > 0){
                createPagination({
                    currentPage: page,
                    totalCount: totalCount,
                    onPageChange: (newPage) => loadNews(newPage),
                    pageSize: 4,
                    containerId: '#pagination'
                });
            }else{
                const container = $('#news-list').empty();
                container.append(`
                    <div class="no-data">
                        검색 결과가 없습니다.
                    </div>
                `);
                $('#news-list').css({
                    "height": "200px"
                })
            }
        }
    });
}

$(document).ready(function(){
    loadNews(1);
});
