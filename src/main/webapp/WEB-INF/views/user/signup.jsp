<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>회원가입</title>
  <link rel="stylesheet" href="/assets/css/user/signup.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/include/header.jsp" %>
    <div class="header-title" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
        <!--나중에 컴포넌트로 바꿀 것.-->
        <img src="/assets/img/header/header-title.png" alt="헤더 타이틀" style="max-width: 100%; height: 320px; display: flex; justify-content: center;">
    </div>
    <div class="signup-form">
        <form action="${pageContext.request.contextPath}/user/signup" method="post" id="board1" onsubmit="return signupCheck();">
            <div class="input-form">
                <div class="signup-input">
                    <label for="id">아이디</label>
                    <input type="text" id="id" name="userId" placeholder="아이디를 입력하세요">
                </div>
                <div class="signup-input">
                    <label for="pwd">비밀번호
                        <span class="error-msg" id="pwd-error"></span>
                    </label>
                    <input type="password" id="pwd" name="userPwd" placeholder="대문자, 특수문자를 포함하여 6~10자리를 입력하세요">
                </div>
                <div class="signup-input">
                    <label for="confirmPwd">비밀번호 확인
                        <span class="error-msg" id="confirm-error"></span>
                    </label>
                    <input type="password" id="confirmPwd" name="confirmPwd" placeholder="비밀번호를 한 번 더 입력하세요">
                </div>
                <div class="signup-input">
                    <label for="userName">이름</label>
                    <input type="text" id="userName" name="userName" placeholder="이름을 입력하세요">
                </div>
                <div class="signup-input">
                    <label for="email">이메일</label>
                    <input type="email" id="email" name="email" placeholder="이메일을 입력하세요">
                </div>
                <div class="signup-input">
                    <label for="phoneNumber">전화번호</label>
                    <input type="tel" id="phoneNumber" name="tel" placeholder="전화번호를 입력하세요">
                </div>
                <div class="signup-btn"><input type="submit" value="완료" alt="완료" disabled></div>
            </div>
        </form>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const id = document.getElementById('id');
            const pwd = document.getElementById('pwd');
            const confirm = document.getElementById('confirmPwd');
            const userName = document.getElementById('userName');
            const email = document.getElementById('email');
            const phoneNumber = document.getElementById('phoneNumber');
            const pwdError = document.getElementById('pwd-error');
            const confirmError = document.getElementById('confirm-error');
            const submitBtn = document.querySelector('.signup-btn input[type="submit"]');
            const iconHTML = `<img src="/assets/img/user/error-message.svg" alt="에러 아이콘">`;

            // 비밀번호 유효성 검사 우선순위 [자릿수 > 대문자 포함 > 특수문자 포함]으로 설정
            function validatePassword() {
                const value = pwd.value.trim();

                if (value.length < 6 || value.length > 10) {
                    pwdError.innerHTML = iconHTML + `6~10자리로 설정해주세요`;
                    return false;
                }

                if (!/[A-Z]/.test(value)) {
                    pwdError.innerHTML = iconHTML + `대문자를 포함해주세요`;
                    return false;
                }

                if (!/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(value)) {
                    pwdError.innerHTML = iconHTML + `특수문자를 포함해주세요`;
                    return false;
                }

                // 에러 없으면 메세지 제거
                pwdError.textContent = "";
                return true;
            }

            // 비밀번호 일치 확인 검사
            function validateConfirmPwd() {
                const confirmVal = confirm.value.trim();
                const pwdVal = pwd.value.trim();

                if (confirmVal && confirmVal !== pwdVal) {
                    confirmError.innerHTML = iconHTML + '비밀번호가 일치하지 않습니다';
                    return false;
                }

                // 에러 없으면 메세지 제거
                confirmError.innerHTML = '';
                return true;
            }

            // 모든 입력 & 유효성 통과 여부 확인해서 버튼 활성화
            function checkInputs() {
                const allFilled =
                    id.value.trim() &&
                    userName.value.trim() &&
                    email.value.trim() &&
                    phoneNumber.value.trim() &&
                    pwd.value.trim() &&
                    confirm.value.trim();

                const pwdOK     = validatePassword();
                const confirmOK = validateConfirmPwd();

                submitBtn.disabled = !(allFilled && pwdOK && confirmOK);
            }

            // 각 필드에 실시간 체크 등록
            [id, userName, email, phoneNumber, pwd, confirm].forEach(el => {
                el.addEventListener('input', checkInputs);
            });

            // 비밀번호 규칙은 포커스 아웃할 시 추가 피드백
            pwd.addEventListener('blur', validatePassword);

            // 최종 form 제출 시에도 검사
            window.signupCheck = function () {
                const allFilled =
                    id.value.trim() &&
                    pwd.value.trim() &&
                    confirm.value.trim() &&
                    userName.value.trim() &&
                    email.value.trim() &&
                    phoneNumber.value.trim();

                const isValidPwd = validatePassword();
                const isValidConfirm = validateConfirmPwd();

                return allFilled && isValidPwd && isValidConfirm;
            };
        });
    </script>

</body>
</html>