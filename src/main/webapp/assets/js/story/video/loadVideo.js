let currentType = null;
let currentKeyword = null;

function loadVideo(page){
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
        url: '/story/videos/page',
        method: 'GET',
        data:{
            page: page,
            type: type,
            keyword: keyword
        },
        success: function(res){
            const list = res.list;
            const totalCount = res.totalCount;

            const container = $('#videos-list').empty();
            list.forEach(videos => {
                container.append(`
                    <div class="video">
                        <div class="thumbnail-img" style="background: url('${videos.thumbnail_url}') no-repeat center center; background-size: cover; " onclick="window.open('${videos.v_url}');"></div>
                        <div class="video-title" onclick="window.open('${videos.v_url}');">${videos.v_title}</div>
                        <div class="video-date">${videos.v_date}</div>
                    </div>
                `);
            });

            if(totalCount > 0){
                createPagination({
                    currentPage: page,
                    totalCount: totalCount,
                    onPageChange: (newPage) => loadVideo(newPage),
                    pageSize: 6,
                    containerId: '#pagination'
                });
            }
        }
    });
}

$(document).ready(function(){
    loadVideo(1);
});