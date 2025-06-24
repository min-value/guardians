package org.baseball.domain.user;
import org.baseball.dto.UserDTO;

public interface UserService {
    // 회원가입
    int registerUser(UserDTO user);
    // 로그인
    UserDTO login(String userId, String userPwd);
    // 마이페이지 내 정보 수정
    void updateUserInfo(UserDTO user);
}