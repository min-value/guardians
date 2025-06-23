package org.baseball.domain.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.baseball.dto.UserDTO;

@Mapper
public interface UserMapper {
    // 회원가입
    int insertUser(UserDTO user);
    // 로그인
    UserDTO login(String userId);
}