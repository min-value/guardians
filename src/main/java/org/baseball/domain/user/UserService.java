package org.baseball.domain.user;
import com.fasterxml.jackson.databind.JsonNode;
import org.baseball.domain.kakao.Tokens;
import org.baseball.dto.UserDTO;

public interface UserService {
    // 회원가입
    void registerUser(UserDTO user);
    // 로그인
    UserDTO login(String userId, String userPwd);
    // 마이페이지 내 정보 수정
    void updateUserInfo(UserDTO user);
    // 마이페이지 회원 탈퇴
    boolean deleteUserByPk(int userPk);
    boolean hasUncancelledReservations(int userPk);
    // 마이페이지 포인트 총합
    int getTotalPoint(int userPk);
    // 아이디 중복체크
    boolean checkUserId(String userId);

    UserDTO getUserInfoByPk(int userPk);
    // 카카오 로그인
    UserDTO findByKakaoToken(String kakaoToken);
    UserDTO loginOrRegisterKakao(JsonNode profile, Tokens tokens);
    void updateKakaoUserInfo(int userPk, String email, String tel, String accessToken, String refreshToken);
}