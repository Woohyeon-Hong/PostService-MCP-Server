package com.hong.PostService_MCP_Server.service;

import java.net.URI;
import java.util.List;

import com.hong.PostService_MCP_Server.dto.LoginRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.hong.PostService_MCP_Server.dto.SignUpRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MCPService {

    private final RestClient restClient;

    @Tool(description = "일반 회원가입을 진행한다. username, password, email, nickname은 필수이다.")
    public URI signUp(
             String username,
             String password, 
           String email,
           String nickname
    ) {

        SignUpRequest signUpRequest = new SignUpRequest(username, password, email, nickname);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(signUpRequest)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        } catch (RestClientException e) {
            throw new RuntimeException("회원 가입 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "어드민 회원 가입을 진행 한다. username, password, email, nickname은 필수이다.")
    public URI signUpAdmin(
             String username,
             String password, 
           String email,
           String nickname
    ) {

        SignUpRequest signUpRequest = new SignUpRequest(username, password, email, nickname);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/users/admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(signUpRequest)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        } catch (RestClientException e) {
            throw new RuntimeException("어드민 회원 가입 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "로그인을 진행한다. username과 password는 필수이다.")
    public String login(
            String username,
            String password
    ) {

        LoginRequest loginRequest = new LoginRequest(username, password);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginRequest)
                    .retrieve()
                    .toBodilessEntity();

            List<String> auths = response.getHeaders().get("Authorization");
            if (auths != null && !auths.isEmpty()) {
                return auths.get(0);
            }

            throw new RuntimeException("로그인 실패: JWT 토큰이 응답에 포함되지 않았습니다.");
        } catch (RestClientException e) {
            throw new RuntimeException("로그인 실패: 잘못된 사용자 이름 또는 비밀번호입니다.");
        }
    }
}
