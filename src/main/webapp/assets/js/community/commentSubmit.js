$(function(){
    $('#commentForm').submit(function (e){
        e.preventDefault();

        $.ajax({
            type: 'POST',
            url: '/community/comment/add',
            data: $(this).serialize(),
            success:function(res){
                loadComment(1);
                $('#commentForm')[0].reset();
            }
        });
    });

    $('#write').keydown(function(e){
        if(e.key === 'Enter' && !e.shiftKey){
            e.preventDefault();
            $('#commentForm').submit();
        }
    })
})
