package org.baseball.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String userPwd;
    private String confirmPwd;
    private String userName;
    private String email;
    private String tel;
}