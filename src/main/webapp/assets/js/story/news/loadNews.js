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
                let month = "";
                if(news.n_month < 10){month = "0"+news.n_month.toString();}
                else{month = news.n_month.toString();}

                let day = "";
                if(news.n_date < 10){day = "0"+news.n_date.toString();}
                else{day = news.n_date.toString();}

                let hour = "";
                if(news.n_hour < 10){hour = "0"+news.n_hour.toString();}
                else{hour = news.n_hour.toString();}

                let minute = "";
                if(news.n_minute < 10){minute = "0"+news.n_minute.toString();}
                else{minute = news.n_minute.toString();}


                const date = news.n_year.toString()+"-"+month+"-"+day+"  "+hour+":"+minute;
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
