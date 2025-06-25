function loadComment(page){
    $.ajax({
        url:'/community/comment',
        method: 'GET',
        data:{
            post_pk: postPk,
            page: page
        },
        success: function (res){
            const commentList = res.list;
            const totalCount = res.totalCount;
            const container = $('#comment-container').empty();
            const deleteForm = $('#deleteComment').empty();
            commentList.forEach(comment =>{
                const date = new Date(comment.c_date);
                const formattedDate = `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2,'0')}-${date.getDate().toString().padStart(2,'0')} ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`;
                const isMine = (comment.user_pk === Number(loginUserPk));

                const html = `
                        <div class="comment-box">
                            <div class="comment-header">
                                <span class="comment-user">${comment.user_name}</span>
                                <span class="comment-date">${formattedDate}</span>
                                <div class="comment-actions">
                                    ${isMine ? `
                                    <button class="comment-edit" onclick="editComment(${comment.comment_pk})">수정</button>
                                    <button class="comment-delete" onclick="deleteComment(${comment.comment_pk})">삭제</button>` : ''}
                                </div>
                            </div>
                            <div class="comment-content">
                                ${comment.c_content}
                            </div>
                        </div>
                    `;

                container.append(html);
            });
            if (totalCount > 0) {
                createPagination({
                    currentPage: page,
                    totalCount: totalCount,
                    onPageChange: (newPage) => loadComment(newPage),
                    pageSize: 5,
                    containerId: '#pagination'
                });
            }
        }
    });
}
$(document).ready(()=>{
    loadComment(1);
});

function editComment(comment_pk){

}

function deleteComment(comment_pk){
    if (confirm("정말 삭제하시겠습니까?")) {
        $.ajax({
            type: 'POST',
            url: '/community/comment/delete',
            data: {comment_pk: comment_pk},
            success: function (res){
                if(res === true){
                    loadComment(1);
                }else{
                    alert("삭제 실패!");
                }
            }
        });
    }
}