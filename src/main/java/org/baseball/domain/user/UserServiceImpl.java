package org.baseball.domain.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.baseball.domain.community.CommunityMapper;
import org.baseball.domain.kakao.Tokens;
import org.baseball.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Autowired
    public CommunityMapper communityMapper;

    @Override
    @Transactional
    public void registerUser(UserDTO user) {
        // 비밀번호 유효성 검사
        if (!isValidPassword(user.getUserPwd())) {
            throw new IllegalArgumentException("비밀번호는 대문자, 특수문자를 포함하여 6~10자리를 입력하세요");
        }

        userMapper.insertUser(user);
    }

    @Override
    public boolean checkUserId(String userId){
        int numOfUser = userMapper.checkUser(userId);
        if(numOfUser == 0) return true;
        else return false;
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        if (password.length() < 6 || password.length() > 10) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[!@#$%^&*()].*")) return false;
        return true;
    }

    @Override
    public UserDTO login(String userId, String userPwd) {
        UserDTO user = userMapper.login(userId);

        System.out.println("userMapper.login() 결과: " + user);

        if (user == null) {
            System.out.println("로그인 실패: 사용자 없음");
            return null;
        }

        System.out.println("DB에서 조회된 비밀번호: " + user.getUserPwd());

        if (user.getUserPwd().equals(userPwd)) {
            System.out.println("비밀번호 일치, 로그인 성공");
            return user;
        } else {
            System.out.println("로그인 실패: 비밀번호 불일치");
            return null;
        }
    }

    // 마이페이지 내 정보 수정
    @Override
    public void updateUserInfo(UserDTO user) {
        userMapper.updateUser(user);
    }

    // 마이페이지 회원 탈퇴
    @Override
    public boolean deleteUserByPk(int userPk) {
        try {
            // 1. 예약 관련 데이터 삭제
            userMapper.deleteReservationsByUser(userPk);
            userMapper.deleteReservationListByUser(userPk);

            // 2. 포인트 기록 삭제
            userMapper.deletePointsByUser(userPk);

            // 3. 커뮤니티 기록 삭제
            userMapper.deleteCommentsByUser(userPk);
            List<Integer> list = communityMapper.selectPostPk(userPk);
            for(int i : list){
                communityMapper.deleteCommentsInPost(i);
                communityMapper.deletePost(i);
            }

            // 4. user 삭제
            int result = userMapper.deleteUser(userPk);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 포인트 내역 조회
    @Override
    public int getTotalPoint(int userPk) {
        Integer result = userMapper.getTotalPoint(userPk);
        return result != null ? result : 0;
    }

    // 카카오 로그인
    @Override
    public UserDTO findByKakaoToken(String kakaoToken) {
        return userMapper.findByKakaoToken(kakaoToken);
    }

    @Override
    public UserDTO loginOrRegisterKakao(JsonNode profile, Tokens tokens) {
        String kakaoIdStr = profile.get("id").asText();
        // 신규 회원 생성
        String nickname = profile.path("properties").path("nickname").asText("카카오유저");
        String email = profile.path("kakao_account").path("email").asText(null);

        // 기존 유저 조회
        UserDTO exist = userMapper.findByKakaoToken(kakaoIdStr);
        if (exist != null) {
            exist.setEmail(tokens != null ? email : exist.getEmail());
            exist.setTel(exist.getTel());
            exist.setAccessToken( tokens.getAccessToken() );
            exist.setRefreshToken( tokens.getRefreshToken() );

            userMapper.updateKakaoUserInfo(
                    exist.getUserPk(),
                    exist.getEmail(),
                    exist.getTel(),
                    exist.getAccessToken(),
                    exist.getRefreshToken()
            );
            return exist;
        }

        UserDTO newUser = new UserDTO();
        newUser.setUserId("kakao_" + kakaoIdStr);
        newUser.setUserName(nickname);
        newUser.setEmail(email);
        newUser.setTel(null);
        newUser.setKakaoToken(kakaoIdStr);
        newUser.setKakao(true);
        newUser.setAccessToken( tokens.getAccessToken() );
        newUser.setRefreshToken( tokens.getRefreshToken() );


        userMapper.insertKakaoUser(newUser);
        return newUser;
    }

    @Override
    public void updateKakaoUserInfo(int userPk, String email, String tel, String accessToken, String refreshToken) {
        userMapper.updateKakaoUserInfo(userPk, email, tel, accessToken, refreshToken);
    }


    @Override
    public UserDTO getUserInfoByPk(int userPk) {
        return userMapper.findUserByPk(userPk);
    }
}
