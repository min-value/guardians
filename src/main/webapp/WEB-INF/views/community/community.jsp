<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String content1Value = "title";
    String content1Text = "제목";
    String content2Value = "writer";
    String content2Text = "작성자";
    String pageTitle = "커뮤니티";

    pageContext.setAttribute("content1Value", content1Value);
    pageContext.setAttribute("content1Text", content1Text);
    pageContext.setAttribute("content2Value", content2Value);
    pageContext.setAttribute("content2Text", content2Text);
    pageContext.setAttribute("pageTitle", pageTitle);
%>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/community/community.css">
<%--    <link rel="stylesheet" href="/assets/css/include/postList.css">--%>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    function loadPage(page){
        const params = new URLSearchParams(window.location.search);
        const type = params.get('type');
        const keyword = params.get('keyword');

        $.ajax({
            url:'/community/page',
            method: 'GET',
            data: {
                page: page,
                type : type,
                keyword: keyword
            },
            success: function(res){
                console.log(res);
                const list = res.list;
                const totalCount = res.totalCount;

                const container = $('#post-container').empty();
                list.forEach(post => {
                    const date = new Date(post.p_date);
                    const formattedDate = `\${date.getFullYear()}-\${(date.getMonth()+1).toString().padStart(2,'0')}-\${date.getDate().toString().padStart(2,'0')} \${date.getHours().toString().padStart(2,'0')}:\${date.getMinutes().toString().padStart(2,'0')}`;
                    container.append(`
                        <div id="post">
                            <div class="title">
                                <a href="/community/post/${'${post.post_pk}'}" class="post-link">\${post.title}</a>
                            </div>
                            <div class="writer">\${post.user_name}</div>
                            <div class="date">\${formattedDate}</div>
                        </div>`
                     );
                });

                const pageCount = Math.ceil(totalCount/10);
                const pagination = $('#pagination').empty();
                // ◀ 이전 버튼
                // if (page >=1) {
                const prev = $('<button class="pageLeft"></button>');
                prev.prop('disabled', page === 1);  // 1페이지면 클릭 막기
                prev.on('click', () => {
                    if (page > 1) loadPage(page - 1);
                });
                pagination.append(prev);
                // }

                // 숫자 버튼 (1 ... 4 5 6 ... 마지막)
                for (let i = 1; i <= pageCount; i++) {
                    if (i === 1 || i === pageCount || Math.abs(i - page) <= 1) {
                        const btn = $(`<button class="page-btn">\${i}</button>`);
                        if (i === page) btn.addClass('active');
                        btn.on('click', () => loadPage(i));
                        pagination.append(btn);
                    } else if (i === page - 2 || i === page + 2) {
                        pagination.append(`<span class="page-ellipsis"></span>`);
                    }
                }

                // ▶ 다음 버튼
                // if (page <= pageCount) {
                const next = $('<button class="pageRight"></button>');
                next.prop('disabled', page === pageCount); // 마지막 페이지면 비활성화
                next.on('click', () => {
                    if (page < pageCount) loadPage(page + 1);
                });
                pagination.append(next);
                // }
            }
        });
    }
    $(document).ready(()=>{
        loadPage(1);
    });
</script>

</head>
<body>
    <%@ include file="../include/header.jsp" %>
    <div class="backgroundWrapper">
        <%@ include file="../include/headerImg.jsp" %>
    </div>
    <div class="content">
        <%@ include file="../include/search.jsp" %>

        <%-- ================ < inventory >================ --%>
            <div class ="inventory">
                <div class="inventoryList">
                    <div class ="all"><span class="clickable" onclick="showPostList()">전체</span></div>
                    <div class ="my"><span class="clickable" onclick="showMyList()">내가 쓴 글</span></div>
                    <div class ="else"></div>
                </div>
                <hr/>
                <div class="postHeader">
                    <div class="title">제목</div>
                    <div class="writer">작성자</div>
                    <div class="date">작성일시</div>
                </div>
                <div id="post-container"></div>
            </div>
        <div class="btnLocation">
            <button class="writeBtn" type="button">글쓰기</button>
        </div>
        <div id="pagination"></div>

        <%-- ============================================ --%>
    </div>
    <div class="footer"></div>
    <%@ include file="../include/footer.jsp" %>
</body>
</html>
