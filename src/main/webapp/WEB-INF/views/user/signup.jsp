<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "회원가입";
%>
<html>
<head>
  <title>신한 가디언즈</title>
  <link rel="stylesheet" href="/assets/css/user/signup.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/include/header.jsp" %>
    <%@ include file="../include/headerImg.jsp" %>
    <div class="signup-form">
        <form action="${pageContext.request.contextPath}/user/signup" method="post" id="board1" onsubmit="return signupCheck();">
            <div class="input-form">
                <div class="signup-input">
                    <label for="id">아이디
                        <span class="success-msg" id="check-Id"></span></label>
                    <div class="idInput">
                        <input type="text" id="id" name="userId" placeholder="아이디를 입력하세요">
                        <button id="checkBtn" type="button" class="double-check">중복체크</button>
                    </div>
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
                    <label for="email">이메일
                        <span class="error-msg" id="email-error"></span></label>
                    <input type="email" id="email" name="email" placeholder="example@email.com">
                </div>
                <div class="signup-input">
                    <label for="phoneNumber">전화번호
                        <span class="error-msg" id="phone-error"></span></label>
                    <input type="tel" id="phoneNumber" name="tel" placeholder="010-XXXX-XXXX">
                </div>
                <div class="signup-btn"><input type="submit" value="완료" alt="완료" disabled></div>
            </div>
        </form>
    </div>

    <script>
        const errorMessage = "${(errorMessage)}";
        if(errorMessage){
            alert(errorMessage);
        }
        document.addEventListener("DOMContentLoaded", () => {
            const id = document.getElementById('id');
            const pwd = document.getElementById('pwd');
            const confirm = document.getElementById('confirmPwd');
            const userName = document.getElementById('userName');
            const email = document.getElementById('email');
            const phoneNumber = document.getElementById('phoneNumber');
            const pwdError = document.getElementById('pwd-error');
            const confirmError = document.getElementById('confirm-error');
            const idCheck = document.getElementById('check-Id');
            const submitBtn = document.querySelector('.signup-btn input[type="submit"]');
            const iconHTML = `<img src="/assets/img/user/error-message.svg" alt="에러 아이콘">`;
            const checkBtn = document.getElementById("checkBtn");
            let isIdChecked = false;
            const regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
            const regPhone = /^(01[016789]|02|0[3-9][0-9])-[0-9]{3,4}-[0-9]{4}$/;

            checkBtn.addEventListener("click", idDoubleCheck);
            id.addEventListener('input', () => {
                isIdChecked = false;
                idCheck.innerHTML = '아이디 중복체크를 해주세요';
                idCheck.classList.add('error-msg');
                idCheck.classList.remove('success-msg');
                checkInputs();
            });

            // 비밀번호 유효성 검사 우선순위 [자릿수 > 대문자 포함 > 특수문자 포함]으로 설정
            function validatePassword() {
                const value = pwd.value.trim();

                if (value === "") {
                    pwdError.innerHTML = "";
                    return false;
                }

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

            // 아이디 중복체크
            function idDoubleCheck() {
                const IdVal = id.value.trim();

                if (!IdVal) {
                    idCheck.innerHTML = iconHTML + '아이디를 입력해주세요.';
                    idCheck.classList.remove('success-msg');
                    idCheck.classList.add('error-msg');
                    isIdChecked = false; // 체크 실패
                    checkInputs();
                    return;
                }

                fetch("/user/signup/check?userId=" + encodeURIComponent(IdVal))
                    .then(res => res.json())
                    .then(data => {
                        if(data === true){
                            idCheck.innerHTML = '사용할 수 있는 아이디입니다.';
                            idCheck.classList.remove('error-msg');
                            idCheck.classList.add('success-msg');
                            isIdChecked = true;
                        }else{
                            idCheck.innerHTML = iconHTML + '중복되는 아이디입니다.';
                            idCheck.classList.remove('success-msg');
                            idCheck.classList.add('error-msg');
                            isIdChecked = false;
                        }
                        checkInputs();
                    })
                    .catch(err => {
                        idCheck.innerHTML = iconHTML + '서버 오류가 발생했습니다.';
                        idCheck.classList.remove('success-msg');
                        idCheck.classList.add('error-msg');
                        isIdChecked = false;
                        checkInputs();
                        console.error(err);
                    });
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

            // 이메일 유효성 검사
            function validEmail(){
                const checkEmail = email.value.trim();
                const emailError = document.getElementById('email-error');

                if (checkEmail === "") {
                    emailError.innerHTML = "";
                    return false;
                }
                if(!regEmail.test(checkEmail)){
                    emailError.innerHTML= iconHTML + '이메일 형식에 맞지 않습니다.';
                    return false;
                }
                emailError.innerHTML = '';
                return true;
            }

            // 전화번호 유효성 검사
            function validPhone(){
                const checkPhone = phoneNumber.value.trim();
                const phoneError = document.getElementById('phone-error');

                if (checkPhone === "") {
                    phoneError.innerHTML = "";
                    return false;
                }
                if(!regPhone.test(checkPhone)){
                    phoneError.innerHTML= iconHTML + '전화번호 형식에 맞지 않습니다.';
                    return false;
                }
                phoneError.innerHTML = '';
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
                const emailOK = validEmail();
                const phoneOK = validPhone();


                submitBtn.disabled = !(allFilled && pwdOK && confirmOK && isIdChecked && emailOK && phoneOK);
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
                const emailOK = validEmail();
                const phoneOK = validPhone();

                return allFilled && isValidPwd && isValidConfirm && isIdChecked && emailOK && phoneOK;
            };
        });
    </script>
</body>
</html>