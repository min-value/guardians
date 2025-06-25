$(document).ready(function () {
    const calendarGrid = document.getElementById("calendar-grid");

    // stadiumPk -> 구장명 매핑
    const stadiumMap = {
        1: "연남",
        2: "고척",
        3: "창원",
        4: "대구",
        5: "광주",
        6: "수원",
        7: "잠실",
        8: "사직",
        9: "대전(신)"
    };

    // 달력 렌더링
    function renderCalendar(year, month) {
        calendarGrid.innerHTML = "";

        const firstDay = new Date(year, month - 1, 1);
        const lastDay = new Date(year, month, 0);
        const startDayOfWeek = firstDay.getDay();
        const totalDays = lastDay.getDate();

        // 앞 빈 셀
        for (let i = 0; i < startDayOfWeek; i++) {
            const emptyCell = document.createElement("div");
            emptyCell.classList.add("calendar-day", "empty");
            calendarGrid.appendChild(emptyCell);
        }

        // 날짜 셀 생성
        for (let d = 1; d <= totalDays; d++) {
            const cell = document.createElement("div");
            cell.classList.add("calendar-day");
            cell.setAttribute("data-day", d);
            cell.innerHTML = `<div class="date-number">${d}</div>`;
            calendarGrid.appendChild(cell);
        }

        // 남는 셀 채우기
        const totalCells = startDayOfWeek + totalDays;
        const remain = 7 - (totalCells % 7 === 0 ? 7 : totalCells % 7);
        for (let i = 0; i < remain; i++) {
            const emptyCell = document.createElement("div");
            emptyCell.classList.add("calendar-day", "empty");
            calendarGrid.appendChild(emptyCell);
        }

        // 경기 정보 불러오기
        $.ajax({
            url: `/games/schedule`,
            method: "GET",
            data: { year: year, month: month },
            success: function(scheduleList) {
                const gameMap = {};

                // 날짜별로 묶기
                scheduleList.forEach(game => {
                    const date = new Date(game.gameDate);
                    const day = date.getDate();

                    if (!gameMap[day]) gameMap[day] = [];
                    gameMap[day].push(game);
                });

                for (let day in gameMap) {
                    const target = document.querySelector(`.calendar-day[data-day="${day}"]`);
                    const games = gameMap[day];

                    if (target && games.length > 0) {
                        const teamPk = games[0].teamPk;
                        const stadiumPk = games[0].stadiumPk;
                        const isSameTeamAndStadium = games.every(g => g.teamPk === teamPk && g.stadiumPk === stadiumPk);

                        let logoImg = "";
                        let locationInfo = "";

                        if (isSameTeamAndStadium) {
                            // 한 번만 출력
                            logoImg = `<img src="/assets/img/games/${teamPk}.png" class="team-logo">`;

                            const timeList = games.map(g => {
                                const date = new Date(g.gameDate);
                                const h = String(date.getHours()).padStart(2, "0");
                                const m = String(date.getMinutes()).padStart(2, "0");
                                return `${h}:${m}`;
                            });
                            const location = stadiumMap[stadiumPk] || "";
                            locationInfo = `<div class="game-info">${location} ${timeList.join(" / ")}</div>`;
                        } else {
                            // 다르면 각각 출력
                            logoImg = games.map(g => `<img src="/assets/img/games/${g.teamPk}.png" class="team-logo">`).join("");
                            locationInfo = games.map(g => {
                                const date = new Date(g.gameDate);
                                const h = String(date.getHours()).padStart(2, "0");
                                const m = String(date.getMinutes()).padStart(2, "0");
                                const loc = stadiumMap[g.stadiumPk] || "";
                                return `<div class="game-info">${loc} ${h}:${m}</div>`;
                            }).join("");
                        }

                        // 결과만 출력 (점수 없이)
                        const resultList = games.map(g => {
                            let cls = "", txt = "";
                            if (g.result === "WIN") { cls = "win"; txt = "승"; }
                            else if (g.result === "LOSE") { cls = "lose"; txt = "패"; }
                            else if (g.result === "DRAW") { cls = "draw"; txt = "무"; }
                            else if (g.result === "CANCEL") { cls = "cancel"; txt = "취"; }
                            return `<div class="game-result ${cls}">${txt}</div>`;
                        }).join("");

                        target.innerHTML += logoImg + locationInfo + resultList;
                    }
                }
            }
        });
    }

    // 초기 렌더링
    const now = new Date();
    const thisYear = now.getFullYear();
    const thisMonth = now.getMonth() + 1;

    $("#year-select").val(thisYear);
    $("#month-select").val(thisMonth);
    renderCalendar(thisYear, thisMonth);

    // 년/월 변경 시
    $("#year-select, #month-select").on("change", function () {
        const y = parseInt($("#year-select").val());
        const m = parseInt($("#month-select").val());
        renderCalendar(y, m);
    });
});
