package org.baseball.domain.user;
import org.baseball.dto.UserDTO;

public interface UserService {
    int registerUser(UserDTO user);
}