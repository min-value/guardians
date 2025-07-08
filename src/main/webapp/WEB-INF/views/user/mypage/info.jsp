<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/assets/css/user/mypage/info.css">
<div class="info-tab">
  <form id="infoForm" class="mypage-form">
    <div class="mypage-input">
      <label for="id">아이디</label>
      <input type="text" id="id" name="userId" value="${user.userId}" readonly>
    </div>
    <div class="mypage-input">
      <label for="userName">이름</label>
      <input type="text" id="userName" name="userName" value="${user.userName}" readonly>
    </div>
    <div class="mypage-input">
      <label for="email">이메일
        <span class="error-msg" id="email-error"></span></label>
      <input type="email" id="email" name="email" value="${user.email}" readonly>
    </div>
    <div class="mypage-input">
      <label for="phoneNumber">전화번호
        <span class="error-msg" id="phone-error"></span></label>
      <input type="tel" id="phoneNumber" name="tel" value="${user.tel}" readonly>
    </div>

    <div class="mypage-btn">
      <button type="button" id="editBtn" class="edit-mode">수정</button>
    </div>

    <button class="withdraw-button" id="openWithdrawModal">탈퇴하기</button>

    <div id="withdrawModal" class="modal" style="display: none;">
      <div class="modal-content">
        <h2 class="modal-title">정말 탈퇴하시겠습니까?</h2>
        <p class="modal-description">
          탈퇴하시면 기록된 모든 정보와 포인트는
          <span class="highlight">완전히 삭제됩니다.</span>
        </p>
        <label class="modal-check">
          <input type="checkbox" id="agreeCheckbox">
          <span class="check-icon"></span>
          <span class="check-text">탈퇴 시 유의사항을 확인하였으며, 모두 동의합니다.</span>
        </label>
        <button type="button" id="cancelWithdraw" class="close-button"></button>
        <button id="confirmWithdraw" class="confirm-button" type="button" disabled>탈퇴</button>
      </div>
    </div>
  </form>
</div>