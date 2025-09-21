package com.hong.PostService_MCP_Server.service;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hong.PostService_MCP_Server.dto.file.FileInfo;
import com.hong.PostService_MCP_Server.dto.file.UploadUrlResponses.UploadUrlResponse;
import com.hong.PostService_MCP_Server.dto.post.Page;
import com.hong.PostService_MCP_Server.dto.post.Page.PostSummaryResponse;
import com.hong.PostService_MCP_Server.dto.post.PostCreateRequest;
import com.hong.PostService_MCP_Server.dto.post.PostCreateRequest.FileCreateRequest;
import com.hong.PostService_MCP_Server.dto.user.LoginRequest;
import com.hong.PostService_MCP_Server.dto.user.PasswordUpdateRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.hong.PostService_MCP_Server.dto.user.SignUpRequest;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class UserService {

    private final RestClient restClient;
    private final FileService fileService;

    private final static String SERVICE_URL = "http://localhost:8080/v2/users";
    public UserService(FileService fileService) {
        this.restClient =  RestClient.builder()
                .baseUrl(SERVICE_URL)
                .defaultHeader("Origin", "http://localhost:8080")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();

        this.fileService = fileService;
    }

    @Tool(description = "username, password, nickname을 받아 일반 회원가입을 진행한다.")
    public URI signUpWithoutEmail(
             @ToolParam(description = "가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가)") String username,
             @ToolParam(description = "가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가)") String password,
             @ToolParam(description = "가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가)") String nickname
    ) {

        SignUpRequest signUpRequest = new SignUpRequest(username, password,null, nickname);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .body(signUpRequest)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        } catch (RestClientException e) {
            throw new RuntimeException("회원 가입 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "username, password, email, nickname을 받아 일반 회원가입을 진행한다.")
    public URI signUpWithEmail(
            @ToolParam(description = "가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가)") String username,
            @ToolParam(description = "가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가)") String password,
            @ToolParam(description = "가입할 사용자의 이메일 주소 (이메일 주소 형식이여야 함, 기존 회원과 중복 불가)") String email,
            @ToolParam(description = "가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가)") String nickname
    ) {

        SignUpRequest signUpRequest = new SignUpRequest(username, password, email, nickname);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .body(signUpRequest)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        } catch (RestClientException e) {
            throw new RuntimeException("회원 가입 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "username, password, nickname을 받아 어드민 회원 가입을 진행 한다.")
    public URI signUpAdminWithoutEmail(
            @ToolParam(description = "가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가)") String username,
            @ToolParam(description = "가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가)") String password,
            @ToolParam(description = "가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가)") String nickname
    ) {

        SignUpRequest signUpRequest = new SignUpRequest(username, password, null, nickname);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/admin")
                    .body(signUpRequest)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        } catch (RestClientException e) {
            throw new RuntimeException("어드민 회원 가입 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "username, password, email, nickname을 받아 어드민 회원 가입을 진행 한다.")
    public URI signUpAdminWithEmail(
            @ToolParam(description = "가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가)") String username,
            @ToolParam(description = "가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가)") String password,
            @ToolParam(description = "가입할 사용자의 이메일 주소 (이메일 주소 형식이어야 함, 기존 회원과 중복 불가)") String email,
            @ToolParam(description = "가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가)") String nickname
    ) {

        SignUpRequest signUpRequest = new SignUpRequest(username, password, email, nickname);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/admin")
                    .body(signUpRequest)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        } catch (RestClientException e) {
            throw new RuntimeException("어드민 회원 가입 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "username과 password을 받아 로그인을 진행한다.")
    public String login(
            String username,
            String password
    ) {

        LoginRequest loginRequest = new LoginRequest(username, password);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/login")
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

    //TODO
    // authorization 헤더를 직접 받는게 아니라 toolContext 사용하도록 변경 -> jwt를 직접 사용하는 것은 위험
    @Tool(description = "authorization 헤더를 받아 로그인한 회원이 작성한 게시글들을 조회한다.")
    public List<PostSummaryResponse> getMemberPosts(String authorization,
                                                    @ToolParam(description = "조회할 페이지 번호 (0부터 시작)") int page,
                                                    @ToolParam(description = "한 페이지에 보여줄 게시글 수")int size) {
        try {
            Page<PostSummaryResponse> postSummaryPage = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/me/posts-simple")
                            .queryParam("page", page)
                            .queryParam("size", size)
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

    @Tool(description = "로그인한 회원의 username을 수정한다.  단, 기존 다른 회원과 중복 불가")
    public void updateUsername(String authorization,
                                 @ToolParam(description = "변경할 사용자 이름 (3-20자 사이, 기존 회원과 중복 불가)") String username) {

        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("username", username);

        update(authorization, updateRequest);
    }

    @Tool(description = "로그인한 회원의 email을 수정한다. 단, 기존 다른 회원과 중복 불가")
    public void updateEmail(String authorization,
                                 @ToolParam(description = "변경할 email 주소 (이메일 주소 형식이어야 함, 기존 회원과 중복 불가)") String email) {

        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("email", email);

        update(authorization, updateRequest);
    }

    @Tool(description = "로그인한 회원의 nickname을 수정한다. 단, 기존 다른 회원과 중복 불가")
    public void updateNickname(String authorization,
                                 @ToolParam(description = "변경할 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가)") String nickname) {

        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("nickname", nickname);

        update(authorization, updateRequest);
    }

    private void update(String authorization, Map<String, String> updateRequest) {
        try {
            restClient
                    .patch()
                    .uri("/me")
                    .header("Authorization", authorization)
                    .body(updateRequest)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("회원 정보 수정 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "로그인한 회원의 비밀번호를 수정한다.")
    public void updatePassword(String authorization,
                               @ToolParam(description = "기존 비밀번호") String currentPassword,
                               @ToolParam(description = "변경할 비밀번호 (6자 이상)") String newPassword) {

        PasswordUpdateRequest request = new PasswordUpdateRequest(currentPassword, newPassword);

        try {
            restClient
                    .patch()
                    .uri("/me/password")
                    .header("Authorization", authorization)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("회원 비밀번호 수정 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "로그인한 회원을 탈퇴한다.")
    public void signOut(String authorization) {
        try {
            restClient
                    .delete()
                    .uri("/me")
                    .header("Authorization", authorization)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("회원 탈퇴 실패: " + e.getMessage(), e);
        }
    }


    @Tool(description = "로그인한 회원으로 제목과 본문을 포함한 새로운 게시글을 작성한다.")
    public URI writePost(
            String authorization,
            @ToolParam(description = "작성할 게시글 제목") String title,
            @ToolParam(description = "작성할 게시글 본문") String content,
            @ToolParam(description = "첨부할 파일들의 목록") List<FileInfo> fileInfoList
    )  {

        List<FileCreateRequest> fileCreateRequests = (fileInfoList != null && !fileInfoList.isEmpty())
                ? assignFileCrateRequests(authorization, fileInfoList) : null;

        PostCreateRequest postCreateRequest = new PostCreateRequest(title, content, fileCreateRequests);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/me/posts")
                    .header("Authorization", authorization)
                    .body(postCreateRequest)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();

        } catch (RestClientException e) {
            throw new RuntimeException("게시글 작성 실패: " + e.getMessage(), e);
        }
    }

    public ArrayList<FileCreateRequest> assignFileCrateRequests(String authorization,
                                                                 List<FileInfo> fileInfoList) {

        List<String> originalFileNames = new ArrayList<>();
        List<File> files = new ArrayList<>();

        for (FileInfo fileInfo : fileInfoList) {
            File file = new File(fileInfo.path());

            if (file.exists()) {
                originalFileNames.add(file.getName());
                files.add(file);
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + fileInfo.path());
            }
        }

        List<UploadUrlResponse> responses = fileService
                .getUploadPresignedUrl(authorization, originalFileNames).results();

        uploadFiles(responses, files);

        ArrayList<FileCreateRequest> fileCreateRequests = new ArrayList<>();

        for (UploadUrlResponse result : responses) {
            fileCreateRequests.add(new FileCreateRequest(result.originalFileName(), result.s3Key()));
        }

        return fileCreateRequests;
    }

    private void uploadFiles(List<UploadUrlResponse> responses, List<File> files) {
        try {
            for (int i = 0; i < responses.size(); i++) {
                UploadUrlResponse response = responses.get(i);
                FileSystemResource fileResource = new FileSystemResource(files.get(i));

                URI presignedUri = URI.create(response.uploadUrl());

                // 파일 업로드 요청
                restClient.put()
                        .uri(presignedUri)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .body(fileResource)
                        .retrieve()
                        .toBodilessEntity();
            }
        } catch (RestClientException e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
    }
}
