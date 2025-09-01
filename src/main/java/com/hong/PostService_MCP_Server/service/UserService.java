package com.hong.PostService_MCP_Server.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.hong.PostService_MCP_Server.dto.post.Page;
import com.hong.PostService_MCP_Server.dto.post.Page.PostSummaryResponse;
import com.hong.PostService_MCP_Server.dto.user.LoginRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.hong.PostService_MCP_Server.dto.user.SignUpRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriBuilder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestClient restClient;
    private final static String SERVICE_URL = "http://localhost:8080/v2/users";
    public UserService() {
        this.restClient =  RestClient.builder()
                .baseUrl(SERVICE_URL)
                .defaultHeader("Origin", "http://localhost:8080")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/geo+json")
                .build();
    }

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
                    .uri("/admin")
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
                    .uri("/login")
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


    @Tool(description = "로그인한 회원이 작성한 게시글들을 조회한다. authorization 헤더는 필수이다.")
    public List<PostSummaryResponse> getMemberPosts(String authorization,
                                                    @ToolParam(description = "조회할 페이지 번호 (0부터 시작)") Optional<Integer> page,
                                                    @ToolParam(description = "한 페이지에 보여줄 게시글 수")Optional<Integer> size) {
        try {
            Page<PostSummaryResponse> postSummaryPage = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/me/posts-simple")
                            .queryParamIfPresent("page", page)
                            .queryParamIfPresent("size", size)
                            .build()
                    )
                    .header("Authorization", authorization)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Page<PostSummaryResponse>>() {});

            return postSummaryPage.content();

        } catch (RestClientException e) {
            throw new RuntimeException("회원 게시글 조회 실패: " + e.getMessage(), e);
        }
    }
}
