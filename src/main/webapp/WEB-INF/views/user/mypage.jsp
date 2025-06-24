<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>마이페이지</title>
    <link rel="stylesheet" href="/assets/css/user/mypage.css">
</head>
<body>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<div class="header-title" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
    <!--나중에 컴포넌트로 바꿀 것.-->
    <img src="/assets/img/header/header-title.png" alt="헤더 타이틀" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
</div>
<div class="mypage-container">
    <div class="mypage-tabs">
        <button class="tab-btn active" data-tab="info">내 정보</button>
        <button class="tab-btn" data-tab="tickets">예매내역</button>
        <button class="tab-btn" data-tab="points">포인트내역</button>
        <button class="tab-btn" data-tab="fairy">승리요정</button>
    </div>

    <div id="tab-content-container"></div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const tabs = document.querySelectorAll('.mypage-tabs .tab-btn');
        const contentContainer = document.getElementById('tab-content-container');

        function bindInfoForm() {
            const form = document.getElementById("infoForm");
            const editBtn = document.getElementById("editBtn");
            if (!form || !editBtn) return;
            let isEditMode = false;
            editBtn.addEventListener("click", () => {
                if (!isEditMode) {
                    form.querySelectorAll("input").forEach(input => {
                        if (input.name !== "userId") input.removeAttribute("readonly");
                    });
                    editBtn.textContent = "저장";
                    editBtn.classList.replace("edit-mode", "save-mode");
                    isEditMode = true;
                } else {
                    const formData = new FormData(form);
                    fetch('/user/update', {
                        method: 'POST',
                        body: new URLSearchParams(formData)
                    })
                        .then(res => res.json())
                        .then(data => {
                            alert(data.message);
                            if (data.success) {
                                form.querySelectorAll("input").forEach(input => input.setAttribute("readonly", true));
                                editBtn.textContent = "수정";
                                editBtn.classList.replace("save-mode", "edit-mode");
                                isEditMode = false;
                            }
                        })
                        .catch(err => {
                            alert("서버 오류가 발생했습니다.");
                            console.error(err);
                        });
                }
            });
        }

        function loadTabContent(tabName) {
            const url = '/user/mypage/' + tabName;
            fetch(url)
                .then(res => res.text())
                .then(html => {
                    contentContainer.innerHTML = html;

                    if (tabName === 'info') {
                        bindInfoForm();
                    }
                })
                .catch(err => console.error(err));
        }


        // 초기 로드
        loadTabContent('info');

        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                const name = tab.getAttribute('data-tab');
                console.log('>>> 탭 클릭: data-tab 속성 =', JSON.stringify(name));
                tabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                loadTabContent(name);
            });
        });
    });
</script>


</body>
</html>
