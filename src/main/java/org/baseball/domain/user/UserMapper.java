package org.baseball.domain.user;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.UserDTO;

@Mapper
public interface UserMapper {
    int insertUser(UserDTO user);
}