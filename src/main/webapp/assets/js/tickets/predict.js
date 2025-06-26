// sessionStorage.setItem("gamePk", 87);
// sessionStorage.setItem("reservelistPk", 1);


let reservelistPk = sessionStorage.getItem("reservelistPk");
let gamePk = sessionStorage.getItem("gamePk");
let user = JSON.parse(sessionStorage.getItem("loginUser")) || {};

let curPredict = null;

gamePk = parseInt(gamePk);
reservelistPk = parseInt(reservelistPk);

$(document).ready(function () {
    console.log("jQuery ready 실행됨");

    if(reservelistPk==null) {
        document.querySelector('.modal').style.display = 'none';
    }

    $.ajax({
        url: `/tickets/predict`,
        method: "GET",
        data: {
            gamePk: gamePk
        },
        dataType: "json",
        success: function (res) {
            console.log("AJAX 응답:", res);
            console.log("res.opponentTeam:", res.opponentTeam);
            if (res.opponentTeam != null) {
                $("#opponentTeam").attr("src", `/assets/img/teamlogos/${res.opponentTeam}.png`);
            }
        },
        error: function (err) {
            console.error("상대 팀 정보를 불러오는 중 오류:", err);
        }
    });

    $("#ourTeam").on("mouseenter", function () {
        $(this).css({ opacity: 1, height: "170px" });
        $("#opponentTeam").css({ opacity: 0.3, height: "150px" });
    }).on("mouseleave", function () {
        if(curPredict == null) {
            $(this).css({opacity: 1, height: "150px"});
            $("#opponentTeam").css({ opacity: 1, height: "150px" });
        }else if (curPredict === 1) {
            $(this).css({ opacity: 0.3, height: "150px" });
            $("#opponentTeam").css({ opacity: 1, height: "170px" });
        }
    });

    $("#opponentTeam").on("mouseenter", function () {
        $(this).css({ opacity: 1, height: "170px" });
        $("#ourTeam").css({ opacity: 0.3, height: "150px" });
    }).on("mouseleave", function () {
        if(curPredict == null) {
            $(this).css({opacity: 1, height: "150px"});
            $("#ourTeam").css({ opacity: 1, height: "150px" });
        }else if (curPredict === 0) {
            $(this).css({ opacity: 0.3, height: "150px" });
            $("#ourTeam").css({ opacity: 1, height: "170px" });
        }
    });

    $("#ourTeam").on("click", function () {
        curPredict = 0;

        const target = document.querySelector(".modal-btn");
        target.disabled = false;
        target.style.backgroundColor = "var(--mainColor)";
        target.style.color = "var(--white)";
    });

    $("#opponentTeam").on("click", function () {
        curPredict = 1;

        const target = document.querySelector(".modal-btn");
        target.disabled = false;
        target.style.backgroundColor = "var(--mainColor)";
        target.style.color = "var(--white)";
    });

    console.log(curPredict);
    console.log(reservelistPk);

    document.querySelector('.modal-btn').addEventListener('click', clickCheerBtn);

});

function clickCheerBtn() {
    console.log(curPredict);
    console.log(reservelistPk);
    $.ajax({
        url: '/tickets/predict',
        method: 'POST',
        data: {
            userPk : user.userPk,
            reservelistPk : reservelistPk,
            predict: curPredict,
        },
        success: function (res) {
            alert("예측 완료!");
            document.querySelector('.modal').style.display = 'none';
        },
        error: function (err) {
            alert("데이터를 저장하지 못했습니다. 다시 시도해주세요.");
            console.error(err);
        }
    });
}