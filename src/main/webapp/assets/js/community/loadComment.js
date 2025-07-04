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
            $('#sendBtn').prop('disabled', false);
            commentList.forEach(comment =>{
                const date = new Date(comment.c_date);
                const formattedDate = `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2,'0')}-${date.getDate().toString().padStart(2,'0')} ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`;
                const isMine = (comment.user_pk === Number(loginUserPk));

                const html = `
                        <div class="comment-box">
                            <div class="comment-header">
                                <div type="text" class="comment-user">${comment.user_name}</div>
                                <div class="comment-date">${formattedDate}</div>
                                <div class="comment-actions">
                                    ${isMine ? `
                                    <button class="comment-edit" onclick="editComment(${comment.comment_pk})" id="edit-btn-${comment.comment_pk}">수정</button>
                                    <button class="comment-delete" onclick="deleteComment(${comment.comment_pk})" id="delete-btn-${comment.comment_pk}">삭제</button>` : ''}
                                </div>
                            </div>
                            <textarea  type="text" class="comment-content" name="content" oninput="resizeTextarea(this)"
                                data-commentpk="${comment.comment_pk}" rows="1" readonly>${comment.c_content}</textarea>
                        </div>
                    `;

                container.append(html);
                container.find('.comment-content').each(function () {
                    resizeTextarea(this);
                });
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
    const contentInput = $(`.comment-content[data-commentpk='${comment_pk}']`);
    const editBtn = $(`#edit-btn-${comment_pk}`);
    const deleteBtn = $(`#delete-btn-${comment_pk}`);

    if(editBtn.text() === '완료'){
        const updatedContent = contentInput.val().trim();
        if(updatedContent === ''){
            alert("내용을 입력해주세요!");
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/community/comment/update',
            data: {
                comment_pk: comment_pk,
                c_content: updatedContent
            },
            success: function (res){
                if(res === true){
                    loadComment(1);
                }else{
                    alert("수정 실패!");
                }
            }
        });
        editBtn.text('수정');
        deleteBtn.css('display', 'inline-block');
    }else{
        contentInput.prop('readonly', false).focus();
        editBtn.text('완료');
        deleteBtn.css('display', 'none');
    }
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

function resizeTextarea(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
}