<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="/assets/css/colors.css"/>
    <link rel="stylesheet" href="/assets/css/admin/admin.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css"/>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
    <script>
        function loadPage(page) {
            const params = new URLSearchParams(window.location.search);
            const status = params.get('status');
            const finalStatus = (status === null || status.trim() === '') ? '0' : status;

            $.ajax({
                url: '/admin/games/allgame/page',
                method: 'GET',
                data: {
                    page: page,
                    status : finalStatus,
                },
                success: function(res) {
                    const list = res.list;
                    const totalCount = res.totalCount;
                    console.log(res);
                    const container = $('#list').empty();

                    list.forEach(dto => {

                        container.append(`
                            <tr>
                                <td>\${dto.no}</td>
                                <td><img class="teamlogo" src="/assets/img/teamlogos/\${dto.ourTeam}.svg" alt="ourTeam">
                                     vs <img class="teamlogo" src="/assets/img/teamlogos/\${dto.opponentTeam}.svg" alt="opponentTeam"></td>
                                <td>\${dto.ourScore}:\${dto.opponentScore}</td>
                                <td>\${dto.result}</td>
                                <td>\${dto.gameDate}</td>
                            </tr>
                        `);
                    });

                    createPagination({
                        currentPage: page,
                        totalCount: totalCount,
                        onPageChange: (newPage) => loadPage(newPage),
                        pageSize: 7,
                        containerId: '#pagination'
                    });
                },
                error: function(err) {
                    alert("데이터를 불러오지 못했습니다.");
                    console.error(err);
                }
            });
        }

        $(document).ready(function () {
            loadPage(1);

            $('select[name="status"]').on('change', function() {
                const selectedStatus = $(this).val();

                // URL 쿼리스트링도 같이 업데이트
                const url = new URL(window.location);
                url.searchParams.set('status', selectedStatus);
                window.history.replaceState({}, '', url);

                // 선택한 status로 1페이지 로드
                loadPage(1);
            });
        });
    </script>
</head>
<body>
<%request.setAttribute("activePage", "gamelist");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="container">
    <div class="title_container">
        <div class="title">
            <img src="/assets/img/icon/list.png" alt="icon"/>
            <p>경기 목록 조회</p>
        </div>
        <div class="logout" onclick="location.href='/user/logout';">
            <span class="logout-text">로그아웃</span>
            <img src="/assets/img/icon/logout.png" alt="icon"/>
        </div>
    </div>
    <div class="content">
        <div class="filter">
            <select name="status">
                <option value="0" <c:if test="${selectedStatus == '0' || empty selectedStatus}">selected</c:if>>전체</option>
                <option value="1" <c:if test="${selectedStatus == '1'}">selected</c:if>>경기 종료</option>
                <option value="2" <c:if test="${selectedStatus == '2'}">selected</c:if>>경기 예정</option>
            </select>
        </div>
        <table class="filter_table">
            <thead>
                <tr>
                    <th>No</th>
                    <th>경기정보</th>
                    <th>스코어</th>
                    <th>결과</th>
                    <th>경기날짜</th>
                </tr>
            </thead>
            <tbody id="list">
            </tbody>
        </table>
        <div id="pagination"></div>
    </div>
</div>
</body>
</html>
