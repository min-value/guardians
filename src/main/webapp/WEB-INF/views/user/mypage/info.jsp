<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/assets/css/user/mypage/info.css">
<div class="info-tab">
  <form id="infoForm" action="${pageContext.request.contextPath}/user/update" method="post" class="mypage-form">
    <div class="mypage-input">
      <label for="id">아이디</label>
      <input type="text" id="id" name="userId" value="${user.userId}" readonly>
    </div>
    <div class="mypage-input">
      <label for="userName">이름</label>
      <input type="text" id="userName" name="userName" value="${user.userName}" readonly>
    </div>
    <div class="mypage-input">
      <label for="email">이메일</label>
      <input type="email" id="email" name="email" value="${user.email}" readonly>
    </div>
    <div class="mypage-input">
      <label for="phoneNumber">전화번호</label>
      <input type="tel" id="phoneNumber" name="tel" value="${user.tel}" readonly>
    </div>

    <div class="mypage-btn">
      <button type="button" id="editBtn" class="edit-mode">수정</button>
    </div>
  </form>
</div>

<script>
  const editBtn = document.getElementById("editBtn");
  const form = document.getElementById("infoForm");
  let isEditMode = false;

  editBtn.addEventListener("click", () => {
    if (!isEditMode) {
      form.querySelectorAll("input").forEach(input => {
        if (input.name !== "userId") {
          input.removeAttribute("readonly");
          input.removeAttribute("disabled");
        }
      });

      editBtn.textContent = "저장";
      editBtn.classList.remove("edit-mode");
      editBtn.classList.add("save-mode");
      isEditMode = true;
    } else {
      form.submit();
    }
  });
</script>
