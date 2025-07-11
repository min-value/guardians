package org.baseball.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int userPk;
    private String userId;
    private String userPwd;
    private String confirmPwd;
    private String userName;
    private String email;
    private String tel;
    private String kakaoToken;
    private String accessToken;
    private String refreshToken;
    private boolean isKakao;
    private boolean isAdmin;
    private int winCnt;
    private int drawCnt;
    private int loseCnt;
    private int totalPoint;
}