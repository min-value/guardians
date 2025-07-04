$(document).ready(function () {

    // 드롭다운 변경 시 AJAX 호출
    $('#year-select, #month-select').on('change', function () {
        const year = $('#year-select').val();
        const month = $('#month-select').val();

        $.ajax({
            url: '/games/details/filter',
            method: 'GET',
            data: { year, month },
            success: function (data) {
                renderGameCards(data.list);
                bindSummaryToggle();

                createPagination({
                    currentPage: 1,
                    totalCount: data.totalCount,
                    pageSize: 6,
                    containerId: '#pagination',
                    onPageChange: (newPage) => loadGamePage(newPage)
                });
            },
            error: function () {
                alert('불러오기 실패');
            }
        });
    });

    bindSummaryToggle();

    const now = new Date();
    $('#year-select').val(now.getFullYear());
    $('#month-select').val(now.getMonth() + 1);

    // DOM 완성 후 트리거
    setTimeout(() => {
        $('#year-select, #month-select').trigger('change');
    }, 100);
});

// 카드 토글
function bindSummaryToggle() {
    $('.summary').off('click').on('click', function (e) {
        e.stopPropagation();

        const $summary = $(this).closest('.summary');
        const $detail = $summary.next('.detail');
        const isOpen = $summary.hasClass('open');

        $('.summary').removeClass('open');
        $('.detail').slideUp();

        if (!isOpen) {
            $summary.addClass('open');
            $detail.slideDown(() => {
                const $container = $detail.find('.custom-bar-chart');
                if ($container.length && !$container.data('loaded')) {
                    drawChart($detail, $container);
                    $container.data('loaded', true);
                }
            });
        }
    });
}

// 카드 렌더링
function renderGameCards(gameList) {
    const container = $("#detail-box");
    container.empty();

    gameList.forEach(game => {
        let ourResultClass = "", oppResultClass = "";
        let myResultText = "", oppResultText = "";
        let myPitcher = "", oppPitcher = "";

        if (game.result === "WIN") {
            ourResultClass = "win";
            oppResultClass = "lose";
            myResultText = "승";
            oppResultText = "패";
            myPitcher = game.winPitcher;
            oppPitcher = game.losePitcher;
        } else if (game.result === "LOSE") {
            ourResultClass = "lose";
            oppResultClass = "win";
            myResultText = "패";
            oppResultText = "승";
            myPitcher = game.losePitcher;
            oppPitcher = game.winPitcher;
        } else if (game.result === "DRAW") {
            ourResultClass = "draw";
            myResultText = "무";
            myPitcher = "-";
        }

        const formattedDate = formatDate(game.date);

        const html = `
            <div class="game-card">
                <div class="summary ${ourResultClass}">
                    <div class="summary-header">
                        <div class="pitcher-with-result">
                            <div class="game-result-circle ${ourResultClass}">${myResultText}</div>
                            <div class="game-pitcher">${myPitcher || ""}</div>
                        </div>
                        <img class="team-logo" src="/assets/img/teamlogos/6.svg" alt="ourlogo" />
                        <div class="game-score">${game.ourScore}</div>

                        <div class="vs-block">
                            <div class="vs-text">VS</div>
                            <div class="vs-date">${formattedDate}</div>
                        </div>

                        <div class="game-score">${game.oppScore}</div>
                        <img class="team-logo" src="/assets/img/teamlogos/${game.oppTeamPk}.svg" alt="opplogo" />

                        <div class="pitcher-with-result" style="${game.result === 'DRAW' ? 'visibility: hidden;' : ''}">
                            <div class="game-result-circle ${oppResultClass}">${oppResultText}</div>
                            <div class="game-pitcher">${oppPitcher || ""}</div>
                        </div>
                    </div>
                    <img class="summary-chevron" src="/assets/img/icon/chevron-down.svg" alt="open-detail">
                </div>

                <div class="detail" style="display: none;">
                    <div class="hit">안타: ${game.ourHit} / ${game.oppHit}</div>
                    <div class="homerun">홈런: ${game.ourHomerun} / ${game.oppHomerun}</div>
                    <div class="strike-out">삼진: ${game.ourStrikeOut} / ${game.oppStrikeOut}</div>
                    <div class="bb">4사구: ${game.ourBb} / ${game.oppBb}</div>
                    <div class="miss">실책: ${game.ourMiss} / ${game.oppMiss}</div>

                    <div class="team-name">
                        <div class="our-team-name">신한</div>
                        <div class="vs">VS</div>
                        <div class="opp-team-name">${game.teamName}</div>
                    </div>

                    <div class="custom-bar-chart"></div>
                </div>
            </div>
        `;
        container.append(html);
    });
}

// 날짜 포맷
function formatDate(dateString) {
    const date = new Date(dateString);
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    return `${month}-${day}`;
}

// 그래프
function drawChart($detail, $container) {
    const labels = ['안타', '홈런', '삼진', '4사구', '실책'];
    const classes = ['hit', 'homerun', 'strike-out', 'bb', 'miss'];
    const ourData = [], oppData = [];

    classes.forEach(className => {
        const text = $detail.find(`.${className}`).text();
        const parts = text.split(':')[1].split('/');
        ourData.push(parseInt(parts[0].trim()));
        oppData.push(parseInt(parts[1].trim()));
    });

    let html = '';
    labels.forEach((label, i) => {
        html += `
            <div class="bar-row">
                <div class="bar-wrap left">
                    <div class="bar our" style="width: ${ourData[i] * 10}px;"></div>
                    <div class="bar-value">${ourData[i]}</div>
                </div>
                <div class="bar-label">${label}</div>
                <div class="bar-wrap right">
                    <div class="bar-value">${oppData[i]}</div>
                    <div class="bar opp" style="width: ${oppData[i] * 10}px;"></div>
                </div>
            </div>
        `;
    });

    $container.html(html);
}

// 페이지네이션
function loadGamePage(page) {
    const year = $('#year-select').val();
    const month = $('#month-select').val();

    $.ajax({
        url: '/games/details/filter',
        method: 'GET',
        data: { year, month, page, size: 6 },
        success: function (data) {
            renderGameCards(data.list);
            bindSummaryToggle();

            createPagination({
                currentPage: page,
                totalCount: data.totalCount,
                pageSize: 6,
                containerId: '#pagination',
                onPageChange: (newPage) => loadGamePage(newPage)
            });
        },
        error: function () {
            alert('불러오기 실패');
        }
    });
}
