package org.baseball.domain.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.baseball.domain.kakao.Tokens;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper om = new ObjectMapper();
    
    // access_token, refresh_token 요청
    public Tokens getTokens(String code) throws JsonProcessingException {
        String url = "https://kauth.kakao.com/oauth/token"
                + "?grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        String resp = rest.postForObject(url, null, String.class);
        JsonNode node = om.readTree(resp);

        return new Tokens(
                node.get("access_token").asText(),
                node.get("refresh_token").asText()
        );
    }

    // access_token으로 프로필 정보 조회
    public JsonNode getProfile(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"properties.nickname\",\"kakao_account.email\"]");

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(body, headers);

        String resp = rest.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                req,
                String.class
        ).getBody();

        return om.readTree(resp);
    }

}