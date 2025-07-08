<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>신한 가디언즈</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/admin.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css"/>

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
    <script>
        function loadPage(page) {
            const params = new URLSearchParams(window.location.search);
            const type = params.get('type');
            const keyword = params.get('keyword');

            $.ajax({
                url: '/admin/tickets/reservation/page',
                method: 'GET',
                data: {
                    page: page,
                    type: type,
                    keyword: keyword
                },
                success: function(res) {
                    const list = res.list;
                    const totalCount = res.totalCount;
                    console.log(res);
                    const container = $('#list').empty();

                    list.forEach(dto => {
                        const seats = (dto.zoneAndSeat && dto.zoneAndSeat.join('<br>')) || '';

                        container.append(`
                            <tr>
                                <td style="padding: 20px 10px">\${dto.no}</td>
                                <td style="padding: 20px 10px">\${dto.name}</td>
                                <td style="padding: 20px 10px">\${dto.reserveNo}</td>
                                <td style="padding: 20px 10px">\${dto.seatCount}</td>
                                <td style="padding: 20px 10px">\${seats}</td>
                                <td style="padding: 20px 10px">\${dto.status}</td>
                                <td style="padding: 20px 10px">\${dto.gameDateFormat}</td>
                                <td style="padding: 20px 10px">\${dto.cancelDateFormat}</td>
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
        });
    </script>
</head>
<body>
<% request.setAttribute("activePage", "reservation"); %>
<jsp:include page="sidebar.jsp" />
<div class="container">
    <div class="title_container">
        <div class="title">
            <img src="/assets/img/icon/list.svg" alt="icon"/>
            <p>예매 목록 조회</p>
        </div>
        <div class="logout" onclick="location.href='/user/logout';">
            <span class="logout-text">로그아웃</span>
            <img src="/assets/img/icon/logout.svg" alt="icon"/>
        </div>
    </div>

    <div class="content">
        <table>
            <thead>
            <tr>
                <th>No</th>
                <th>예매자</th>
                <th>예매번호</th>
                <th>매수</th>
                <th>좌석번호</th>
                <th>상태</th>
                <th>경기날짜</th>
                <th>취소신청일</th>
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
