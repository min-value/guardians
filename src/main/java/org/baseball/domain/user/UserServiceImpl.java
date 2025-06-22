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
}
