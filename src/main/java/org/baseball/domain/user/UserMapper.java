package org.baseball.domain.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.baseball.dto.UserDTO;

@Mapper
public interface UserMapper {
    // 회원가입
    void insertUser(UserDTO user);
    // 중복체크
    int checkUser(String userId);
    // 로그인
    UserDTO login(String userId);
    // 마이페이지 내 정보 수정
    void updateUser(UserDTO user);
    // 포인트 총합 조회
    Integer getTotalPoint(@Param("userPk") int userPk);
    // 포인트 총합 업데이트
    void updateTotalPoint(@Param("userPk") int userPk, @Param("amount") int amount);
    // 마이페이지 승리요정 유저 조회
    UserDTO findUserByPk(@Param("userPk") int userPk);
    // 카카오 토큰으로 DB에서 유저 조회
    UserDTO findByKakaoToken(@Param("kakaoToken") String kakaoToken);
    // 신규 유저 정보 DB에 저장
    void insertKakaoUser(UserDTO user);
    // 카카오 유저가 이메일 변경 시 DB도 업데이트
    void updateKakaoUserInfo(@Param("userPk") int userPk,
                             @Param("email") String email,
                             @Param("tel") String tel,
                             @Param("accessToken") String accessToken,
                             @Param("refreshToken") String refreshToken);
}