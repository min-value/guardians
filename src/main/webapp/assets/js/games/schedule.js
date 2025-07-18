$(document).ready(function () {
    const calendarGrid = document.getElementById("calendar-grid");

    // 달력 렌더링
    function renderCalendar(year, month) {
        calendarGrid.innerHTML = "";

        const firstDay = new Date(year, month - 1, 1);
        const lastDay = new Date(year, month, 0);
        const startDayOfWeek = firstDay.getDay();
        const totalDays = lastDay.getDate();

        const today = new Date();

        // 앞쪽 날짜 없는 셀
        for (let i = 0; i < startDayOfWeek; i++) {
            const emptyCell = document.createElement("div");
            emptyCell.classList.add("calendar-day", "empty");
            calendarGrid.appendChild(emptyCell);
        }

        // 날짜 셀 생성
        for (let d = 1; d <= totalDays; d++) {
            const cell = document.createElement("div");
            cell.classList.add("calendar-day");
            var dateNum = "";
            if(d<10){dateNum = "0" + d.toString();}
            else dateNum = d.toString();

            const isToday = (
                year === today.getFullYear() &&
                month === (today.getMonth() + 1) &&
                d === today.getDate()
            );
            if (isToday) {
                cell.classList.add("today");
                cell.innerHTML = `
                <div class="date-line-t"></div>
                <div class="date-number-t">${dateNum}</div>`;
            }
            else{
                cell.innerHTML = `
                <div class="date-line"></div>
                <div class="date-number">${dateNum}</div>`;
            }
            cell.setAttribute("data-day", d);
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
                if (!scheduleList || scheduleList.length === 0) {
                    alert(`${month}월에는 편성된 경기가 없습니다.`);
                    return;
                }
                const gameMap = {};

                // 더블헤더 고려해서 일정 나누기
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
                            // 더블헤더 시 로고 한 번만 출력
                            logoImg = `<img src="/assets/img/teamlogos/${teamPk}.svg" class="team-logo">`;

                            const timeList = games.map(g => {
                                const date = new Date(g.gameDate);
                                const h = String(date.getHours()).padStart(2, "0");
                                const m = String(date.getMinutes()).padStart(2, "0");
                                return `${h}:${m}`;
                            });
                            const location = games[0].location || "";
                            locationInfo = `<div class="game-info">${location} ${timeList.join(" / ")}</div>`;
                        } else {
                            // 더블헤더 아닐 시
                            logoImg = games.map(g => `<img src="/assets/img/games/${g.teamPk}.png" class="team-logo">`).join("");
                            locationInfo = games.map(g => {
                                const date = new Date(g.gameDate);
                                const h = String(date.getHours()).padStart(2, "0");
                                const m = String(date.getMinutes()).padStart(2, "0");
                                const loc = g.location || "";
                                return `<div class="game-info">${loc} ${h}:${m}</div>`;
                            }).join("");
                        }

                        // 결과 출력
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
