package org.baseball.domain.user;

import org.baseball.domain.user.UserMapper;
import org.baseball.dto.UserDTO;
import org.baseball.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public int registerUser(UserDTO user) {
        // 비밀번호 유효성 검사
        if (!isValidPassword(user.getUserPwd())) {
            throw new IllegalArgumentException("비밀번호는 대문자, 특수문자를 포함하여 6~10자리를 입력하세요");
        }

        return userMapper.insertUser(user);
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
}
