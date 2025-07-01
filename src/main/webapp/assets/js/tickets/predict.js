let curPredict = null;
let reservelistPk = JSON.parse(sessionStorage.getItem("reservelistPk"));
let gameInfo = JSON.parse(sessionStorage.getItem("gameInfo"));

$(document).ready(function () {
    $("#opponentTeam").attr("src", `/assets/img/teamlogos/${gameInfo.oppTeamPk}.png`);

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
            sessionStorage.clear();
            window.location.href = '/tickets/all';
        },
        error: function (err) {
            alert("데이터를 저장하지 못했습니다. 다시 시도해주세요.");
            console.error(err);
        }
    });
}