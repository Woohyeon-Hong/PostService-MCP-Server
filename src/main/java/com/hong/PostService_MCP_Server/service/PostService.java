package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.comment.CommentPage;
import com.hong.PostService_MCP_Server.dto.file.FileInfo;
import com.hong.PostService_MCP_Server.dto.post.Page;
import com.hong.PostService_MCP_Server.dto.post.Page.PostSummaryResponse;
import com.hong.PostService_MCP_Server.dto.post.PostCreateRequest;
import com.hong.PostService_MCP_Server.dto.post.PostCreateRequest.FileCreateRequest;
import com.hong.PostService_MCP_Server.dto.post.PostDetailResponse;
import com.hong.PostService_MCP_Server.dto.post.PostUpdateRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hong.PostService_MCP_Server.dto.comment.CommentPage.*;

@Service
public class PostService {

    private final RestClient restClient;
    private final UserService userService;
    private final static String SERVICE_URL = "http://localhost:8080/v2/posts";

    @Autowired
    public PostService(UserService userService) {
        this.restClient = RestClient.builder()
                .baseUrl(SERVICE_URL)
                .defaultHeader("Origin", "http://localhost:8080")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();

        this.userService = userService;
    }

    @Tool(description = "전체 게시글 목록을 조회한다.")
    public List<PostSummaryResponse> getAllPosts(
            @ToolParam(description = "조회할 페이지 번호 (0부터 시작)") int page,
            @ToolParam(description = "한 페이지에 보여줄 게시글 수")int size) {

        try {
            Page<PostSummaryResponse> response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<Page<PostSummaryResponse>>() {});

            return response.content();
        } catch (RestClientException e) {
            throw new RuntimeException("전체 게시글 목록 조회: " + e.getMessage(), e);
        }
    }

    @Tool(description = "게시글의 id를 받아, 해당 게시글을 상세히 조회한다.")
    public PostDetailResponse getPost(Long postId) {
        try {
            return restClient
                    .get()
                    .uri("/{postId}", postId)
                    .retrieve()
                    .body(PostDetailResponse.class);
        } catch (RestClientException e) {
            throw new RuntimeException("게시글 상세 조회 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "사용자 닉네임과 게시글 제목을 입력 받아, 검색한 닉네임 문자열이 닉네임에 포함된 사용자가 작성했거나 검색한 제목 문자열을 포함하는 제목을 가지는 게시글 목록을 검색한다.")
    public List<PostSummaryResponse> searchPostsWithWriterAndTitle(
            @ToolParam(description = "게시글 작성자의 닉네임") String writer,
            @ToolParam(description = "게시글 제목") String title
    ) {
        try {
            Page<PostSummaryResponse> response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("writer", writer)
                            .queryParam("title", title)
                            .build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<Page<PostSummaryResponse>>() {
                    });

            return response.content();
        } catch (RestClientException e) {
            throw new RuntimeException("게시글 검색 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "게시글 제목을 입력 받아, 검색한 제목 문자열을 포함하는 제목을 가지는 게시글 목록을 검색한다.")
    public List<PostSummaryResponse> searchPostsWithTitle(
            @ToolParam(description = "게시글 제목") String title
    ) {
        try {
            Page<PostSummaryResponse> response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("title", title)
                            .build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<Page<PostSummaryResponse>>() {
                    });

            return response.content();
        } catch (RestClientException e) {
            throw new RuntimeException("게시글 검색 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "사용자 닉네임을 입력 받아, 검색한 닉네임 문자열이 닉네임에 포함된 사용자가 작성한 게시글 목록을 검색한다.")
    public List<PostSummaryResponse> searchPostsWithWriter(
            @ToolParam(description = "게시글 작성자의 닉네임") String writer
    ) {
        try {
            Page<PostSummaryResponse> response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("writer", writer)
                            .build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<Page<PostSummaryResponse>>() {
                    });

            return response.content();
        } catch (RestClientException e) {
            throw new RuntimeException("게시글 검색 실패: " + e.getMessage(), e);
        }
    }

    //TODO
    //file 관련 기능 추가
    @Tool(description = "로그인한 회원으로 변경하고자 하는 게시글의 id, 변경하려는 제목 그리고 변경하려는 본문을 받아 게시글을 수정한다.")
    public void updatePostWithTitleAndContent(
            String authorization,
            @ToolParam(description = "수정할 게시글 id") Long postId,
            @ToolParam(description = "변경하고자 하는 제목") String title,
            @ToolParam(description = "변경하고자 하는 본문") String content) {

        PostUpdateRequest request = new PostUpdateRequest(title, content);

        try {
            restClient
                    .patch()
                    .uri("/{postId}", postId)
                    .header("Authorization", authorization)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("게시글 수정 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "로그인한 회원으로 변경하고자 하는 게시글의 id, 변경하려는 제목 그리고 변경하려는 본문을 받아 게시글을 수정한다.")
    public void updatePostWithTitle(
            String authorization,
            @ToolParam(description = "수정할 게시글 id") Long postId,
            @ToolParam(description = "변경하고자 하는 제목") String title
    ) {

        PostUpdateRequest request = new PostUpdateRequest(title, null);

        try {
            restClient
                    .patch()
                    .uri("/{postId}", postId)
                    .header("Authorization", authorization)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("게시글 수정 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "로그인한 회원으로 변경하고자 하는 게시글의 id, 변경하려는 제목 그리고 변경하려는 본문을 받아 게시글을 수정한다.")
    public void updatePostWithContent(
            String authorization,
            @ToolParam(description = "수정할 게시글 id") Long postId,
            @ToolParam(description = "변경하고자 하는 본문") String content) {

        PostUpdateRequest request = new PostUpdateRequest(null, content);

        try {
            restClient
                    .patch()
                    .uri("/{postId}", postId)
                    .header("Authorization", authorization)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("게시글 수정 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "새로 파일을 첨부할 게시글의 아이디와 첨부할 파일들의 FileInfoList를 받아, 로그인한 회원으로 해당 게시글에 dto의 파일들을 첨부한다.")
    public void addAttachments(
            String authorization,
            @ToolParam(description = "파일을 추가할 게시글 id") Long postId,
            @ToolParam(description = "추가하고자 하는 파일들의 dto 리스트")  List<FileInfo> fileInfoList) {

        List<FileCreateRequest> fileCreateRequests = (fileInfoList != null && !fileInfoList.isEmpty())
                ?  userService.assignFileCrateRequests(authorization, fileInfoList) : null;

        Map<String, List<FileCreateRequest>> map = new HashMap<>();
        map.put("addFiles", fileCreateRequests);

        try {
            ResponseEntity<Void> response = restClient
                    .patch()
                    .uri("/{postId}", postId)
                    .header("Authorization", authorization)
                    .body(map)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("새로운 파일 첨부 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "삭제할 파일이 첨부된 게시글의 아이디와 파일의 아이디를 받아, 로그인한 회원으로 해당 파일을 게시글에서 삭제한다.")
    public void removeAttachments(
            String authorization,
            @ToolParam(description = "파일을 삭제할 게시글 id") Long postId,
            @ToolParam(description = "삭제하고자 하는 파일 id 목록")  List<Long> fileIds) {
        Map<String, List<Long>> map = new HashMap<>();
        map.put("removeFileIds", fileIds);

        try {
            restClient
                    .patch()
                    .uri("/{postId}", postId)
                    .header("Authorization", authorization)
                    .body(map)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("파일 첨부 실패: " + e.getMessage(), e);
        }
    }
    @Tool(description = "로그인한 회원으로 넙겨받은 id의 게시글을 삭제한다. 삭제는 소프트 삭제 방식으로 이뤄지며, 게시글 삭제 시, 게시글에 딸린 댓글과 파일들도 소프트 삭제된다.")
    public void deletePost(
            String authorization,
            @ToolParam(description = "삭제할 게시글 id") Long postId
    ) {

        try {
            restClient
                    .delete()
                    .uri("/{postId}", postId)
                    .header("Authorization", authorization)
                    .retrieve()
                    .toBodilessEntity();
        }  catch (RestClientException e) {
            throw new RuntimeException("게시글 삭제 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "댓글을 달 게시글의 id, 댓글을 달 내용을 받아, 로그인한 회원으로 게시글에 댓글을 작성한다.")
    public URI writeComment(
            String authorization,
            @ToolParam(description = "댓글을 달 게시글 id") Long postId,
            @ToolParam(description = "댓글을 달 내용") String content
    ) {

        Map<String, String> map = new HashMap<>();
        map.put("content", content);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/{postId}/comments", postId)
                    .header("Authorization", authorization)
                    .body(map)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        }  catch (RestClientException e) {
            throw new RuntimeException("댓글 작성 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "댓글 목록을 조회할 게시글 id를 받아, 해당 게시글에 달린 댓글 목록을 조회한다.")
    public List<CommentResponse> getCommentsOfPost(
            @ToolParam(description = "댓글 목록을 조회할 게시글 id") Long postId
    ) {
        try {
            CommentPage<CommentResponse> responsePage = restClient
                    .get()
                    .uri("/{postId}/comments", postId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<CommentPage<CommentResponse>>() {
                    });

            return responsePage.content();
        }  catch (RestClientException e) {
            throw new RuntimeException("댓글 목록 조회 실패: " + e.getMessage(), e);
        }
    }
}
