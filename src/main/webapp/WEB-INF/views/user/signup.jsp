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

                <div class="agree-container">
                    <div class="agree-item">
                        <input type="checkbox" id="agreeAll">
                        <label for="agreeAll">모두 동의</label>
                    </div>
                    <hr class="divider-line" />
                    <div class="agree-item sub">
                        <input type="checkbox" class="agree" id="agree1" data-required>
                        <label for="agree1">이용약관 동의(필수)
                            <img src="${pageContext.request.contextPath}/assets/img/icon/chevron-right.svg" class="arrow-icon">
                        </label>
                        <div class="agree-content">
                            <h2>서비스 이용약관</h2>
                            <p>본 약관은 SHINHAN GUARDIANS BASEBALL CLUB(이하 ‘신한 가디언즈’)이 제공하는 공식 웹사이트(이하 ‘사이트’)의 이용과 관련하여, 이용자의 권리와 의무, 책임사항 및 기타 필요한 사항을 규정함을 목적으로 합니다.</p>

                            <h3>제1조 (목적)</h3>
                            <p>이 약관은 신한 가디언즈가 제공하는 모든 온라인 서비스(정보 제공, 커뮤니티 등)의 이용조건, 절차, 회원과 운영자의 권리, 의무 및 책임사항을 규정합니다.</p>

                            <h3>제2조 (회원 가입 및 이용)</h3>
                            <ul>
                                <li>이용자는 본 약관에 동의한 후 사이트에서 제공하는 회원가입 절차를 완료함으로써 회원으로 등록됩니다.</li>
                                <li>회원은 등록된 계정을 통해 본 사이트가 제공하는 콘텐츠, 커뮤니티, 티켓 예매, 이벤트 참여 등의 서비스를 이용할 수 있습니다.</li>
                                <li>회원가입 시 허위정보를 입력한 경우, 서비스 이용이 제한될 수 있습니다.</li>
                            </ul>

                            <h3>제3조 (서비스의 제공 및 변경)</h3>
                            <ul>
                                <li>사이트는 팀 소식, 경기 일정, 선수 정보, 커뮤니티 등의 콘텐츠를 제공합니다.</li>
                                <li>서비스 내용은 사전 공지 없이 일부 변경될 수 있으며, 변경된 사항은 공지사항을 통해 안내합니다.</li>
                            </ul>

                            <h3>제4조 (이용자의 의무)</h3>
                            <ul>
                                <li>회원은 서비스 이용 시 법령 및 공공질서에 반하는 행위를 해서는 안 됩니다.</li>
                                <li>사이트 내 게시물은 타인의 권리를 침해하지 않아야 하며, 악성 댓글이나 허위 정보 작성 시 삭제 또는 이용 제한이 될 수 있습니다.</li>
                            </ul>

                            <h3>제5조 (저작권)</h3>
                            <p>사이트 내 모든 콘텐츠(텍스트, 이미지, 영상 등)의 저작권은 신한 가디언즈 또는 해당 저작권자에게 있으며, 무단 복제·배포를 금지합니다.</p>

                            <h3>제6조 (책임의 제한)</h3>
                            <p>사이트는 천재지변, 시스템 장애 등 불가항력적 사유로 인한 서비스 중단에 대해 책임을 지지 않습니다.</p>

                            <h3>제7조 (약관의 변경)</h3>
                            <p>본 약관은 필요 시 변경될 수 있으며, 변경된 약관은 공지사항을 통해 사전 안내합니다. 회원은 변경된 약관에 동의하지 않을 경우 서비스 이용을 중단할 수 있습니다.</p>
                        </div>
                    </div>
                    <div class="agree-item sub">
                        <input type="checkbox" class="agree" id="agree2" data-required>
                        <label for="agree2">개인정보 수집 및 이용동의(필수)
                            <img src="${pageContext.request.contextPath}/assets/img/icon/chevron-right.svg" class="arrow-icon">
                        </label>
                        <div class="agree-content">
                            <h2>개인정보 처리방침</h2>
                            <p>
                                SHINHAN GUARDIANS BASEBALL CLUB(이하 ‘신한 가디언즈’)은 회원의
                                개인정보를 소중히 여기며, 관련 법령에 따라 개인정보를 안전하게
                                보호하고 관리하기 위해 아래와 같은 방침을 수립하고 준수합니다.
                            </p>

                            <h3>1. 수집하는 개인정보 항목</h3>
                            <p>사이트는 회원가입, 문의, 예매 및 이벤트 응모 등을 위해 다음과 같은 정보를 수집할 수 있습니다.</p>
                            <ul>
                                <li>필수정보: 이름, 아이디, 비밀번호, 이메일, 휴대전화번호</li>
                                <li>선택정보: 생년월일, 주소, 관심팀, 사진</li>
                                <li>서비스 이용기록, 접속 로그, 쿠키, 접속 IP 등</li>
                            </ul>

                            <h3>2. 개인정보 수집 목적</h3>
                            <p>수집한 개인정보는 다음의 목적을 위해 활용됩니다.</p>
                            <ul>
                                <li>회원관리: 본인확인, 가입의사 확인, 불만처리</li>
                                <li>서비스 제공: 경기 정보 제공, 티켓 예매, 커뮤니티 운영</li>
                                <li>마케팅 및 홍보: 이벤트 안내, 만족도 조사</li>
                            </ul>

                            <h3>3. 개인정보 보유 및 이용기간</h3>
                            <p>회원 탈퇴 시까지 개인정보를 보관하며, 탈퇴 요청 또는 수집 목적 달성 시 지체 없이 파기합니다. 단, 관련 법령에 따라 일정 기간 보존이 필요한 정보는 해당 기간 동안 보존합니다.</p>

                            <h3>4. 개인정보 제3자 제공</h3>
                            <p>회원의 개인정보는 원칙적으로 외부에 제공하지 않으며, 아래의 경우에만 예외로 제공됩니다.</p>
                            <ul>
                                <li>회원의 사전 동의를 받은 경우</li>
                                <li>법령에 의해 요구되는 경우</li>
                            </ul>

                            <h3>5. 개인정보의 안전성 확보 조치</h3>
                            <p>신한 가디언즈는 개인정보 보호를 위해 다음과 같은 조치를 취하고 있습니다.</p>
                            <ul>
                                <li>개인정보 암호화 저장 및 관리</li>
                                <li>접근권한 최소화 및 정기적 점검</li>
                                <li>보안 프로그램 설치 및 주기적인 업데이트</li>
                            </ul>

                            <h3>6. 이용자의 권리</h3>
                            <p>회원은 언제든지 자신의 개인정보를 조회하거나 수정할 수 있으며, 삭제를 요청할 수 있습니다. 개인정보와 관련한 문의는 고객센터 또는 관리자 이메일로 연락해 주세요.</p>

                            <h3>7. 개인정보 보호책임자</h3>
                            <p>개인정보 보호 관련 문의는 아래로 연락주시면 신속하게 답변드리겠습니다.</p>
                            <ul>
                                <li>책임자: 오수빈</li>
                                <li>이메일: bread0930@gmail.com</li>
                            </ul>
                        </div>
                    </div>
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
            const agreeAll   = document.getElementById('agreeAll');
            const agreeBoxes = Array.from(document.querySelectorAll('.agree'));
            const requiredBoxes = agreeBoxes.filter(cb => cb.dataset.required !== undefined);
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

            // 전체 동의
            agreeAll.addEventListener('change', () => {
                agreeBoxes.forEach(cb => cb.checked = agreeAll.checked);
                toggleSubmit();
            });

            // 개별 동의
            agreeBoxes.forEach(cb => {
                cb.addEventListener('change', () => {
                    agreeAll.checked = agreeBoxes.every(c => c.checked);
                    toggleSubmit();
                });
            });

            // 약관 동의 텍스트 자세히 열기
            document.querySelectorAll('.agree-item.sub .arrow-icon').forEach(arrow => {
                const item    = arrow.closest('.agree-item.sub');
                const content = item.querySelector('.agree-content');

                content.style.maxHeight = '0';
                arrow.style.transition  = 'transform 0.3s';

                arrow.addEventListener('click', e => {
                    e.stopPropagation();
                    e.preventDefault();

                    const isOpen = content.style.maxHeight !== '0px';
                    content.style.maxHeight = isOpen ? '0' : '100%';
                    arrow.style.transform = isOpen ? 'rotate(0)' : 'rotate(90deg)';
                });
            });

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
                const agreeOK  = requiredBoxes.every(cb => cb.checked);

                submitBtn.disabled = !(allFilled && pwdOK && confirmOK && isIdChecked && emailOK && phoneOK && agreeOK);
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