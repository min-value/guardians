<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/colors.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/admin.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/include/pagination.css">

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/include/pagination.js"></script>
    <script>
        let gameNo = null;
        let currentPage = 1;
        function openModal(gameNo){
            document.querySelector('#modal-gameNo').value = gameNo;

            fetch('/admin/tickets/gameinfo?gameNo=' + gameNo)
                .then(response => response.json())
                .then(data => {
                    // 모달 내부에 값 삽입
                    document.querySelector('#modal-opponentTeam').value = data.opponentTeamName;
                    document.querySelector('#modal-gameDate').value = data.gameDate;
                    document.querySelector('#modal-stadium').value = data.stadium;

                    // 모달 열기
                    document.querySelector('.modal').style.display = 'flex';
                });

        }

        function saveHomeGame() {
            const gameNo = document.querySelector('#modal-gameNo').value;
            const start = document.querySelector("#startDate").value;
            const end = document.querySelector("#endDate").value;

            if (!start || !end) {
                alert("날짜를 입력해주세요.");
                return false;
            }

            if (start > end) {
                alert("시작일과 마감일을 확인해주세요.");
                return false;
            }

            $.ajax({
                url: '/admin/tickets/addgame/page',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    gameNo: gameNo,
                    startTime: start,
                    endTime: end
                }),
                success: function(res) {
                    alert("등록 완료");
                    document.querySelector('.modal').style.display = 'none';
                    loadPage(currentPage);
                },
                error: function() {
                    alert("실패하였습니다. 다시 시도해 주세요.");
                }
            });

            return false; // form submit 방지
        }

        function loadPage(page) {
            currentPage = page;
            $.ajax({
                url: '/admin/games/allgame/page',
                method: 'GET',
                data: {
                    page: page,
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
                                <td><img class="teamlogo" src="/assets/img/teamlogos/\${dto.ourTeam}.png" alt="ourTeam">
                                    vs <img class="teamlogo" src="/assets/img/teamlogos/\${dto.opponentTeam}.png" alt="opponentTeam"></td>
                                <td>\${dto.ourScore}:\${dto.opponentScore}</td>
                                <td>\${dto.result}</td>
                                <td>\${dto.gameDate}</td>
                                <td>
                                    <input class="save_btn" type="button" value="예매등록" onclick="openModal(\${dto.gameNo})">
                                </td>
                            </tr>
                        `);
                    });

                    createPagination({
                        currentPage: page,
                        totalCount: totalCount,
                        onPageChange: (newPage) => loadPage(newPage),
                        pageSize: 10,
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
<%request.setAttribute("activePage", "addgames");%>
<jsp:include page="sidebar.jsp" ></jsp:include>
<div class="container">
    <div class="title_container">
        <div class="title">
            <img src="/assets/img/icon/schedule.png" alt="icon"/>
            <p>경기 예매 등록</p>
        </div>
        <div class="logout">
            <a>로그아웃</a>
            <img src="/assets/img/icon/logout.png" alt="icon"/>
        </div>
    </div>
    <div class="content">
        <table>
            <thead>
                <tr>
                    <th>No</th>
                    <th>경기정보</th>
                    <th>스코어</th>
                    <th>결과</th>
                    <th>경기날짜</th>
                    <th>예매등록</th>
                </tr>
            </thead>
            <tbody id="list">
            </tbody>
        </table>
        <div id="pagination"></div>

    </div>
    <div class="modal">
        <div class="modal-body">
            <h3>예매 등록</h3>
                <input type="hidden" name="gameNo" id="modal-gameNo">
                <div class="form-row">
                    <label for="modal-opponentTeam">원정팀:</label>
                    <input type="text" id="modal-opponentTeam" readonly>
                </div>
                <div class="form-row">
                    <label for="modal-gameDate">경기날짜:</label>
                    <input type="text" id="modal-gameDate" readonly>
                </div>
                <div class="form-row">
                    <label for="modal-stadium">경기장:</label>
                    <input type="text" id="modal-stadium" readonly>
                </div>
                <div class="form-row">
                    <label for="startDate">예매 시작일:</label>
                    <input type="datetime-local" name="startTime" id="startDate">
                </div>
                <div class="form-row">
                    <label for="endDate">예매 마감일:</label>
                    <input type="datetime-local" name="endTime" id="endDate">
                </div>
                <div class="btn_container">
                    <input type="button" value="취소" onclick="document.querySelector('.modal').style.display='none'" class="close_btn">
                    <input type="button" value="저장" class="save_btn" id="add_game_btn" onclick="saveHomeGame()">
                </div>
        </div>
    </div>
</div>
</body>
</html>
