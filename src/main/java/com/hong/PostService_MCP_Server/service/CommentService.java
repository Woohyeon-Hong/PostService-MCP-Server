package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.comment.ReplyCreateRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommentService {

    private final RestClient restClient;
    private final static String SERVICE_URL = "http://localhost:8080/v2/comments";

    public CommentService() {
        this.restClient = RestClient.builder()
                .baseUrl(SERVICE_URL)
                .defaultHeader("Origin", "http://localhost:8080")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Tool(description = "대댓글을 작성할 댓글과 그 게시글의 id, 그리고 작성할 내용을 받아 대댓글을 작성한다.")
    public URI writeReply(
            String authorization,
            @ToolParam(description = "대댓글을 작성할 댓글 id") Long parentCommentId,
            @ToolParam(description = "대댓글을 작성할 게시글 id") Long postId,
            @ToolParam(description = "대댓글을 작성할 내용") String content
    ) {

        ReplyCreateRequest request = new ReplyCreateRequest(postId, content);

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri("/{parentCommentId}/replies", parentCommentId)
                    .header("Authorization", authorization)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            return response.getHeaders().getLocation();
        } catch (RestClientException e) {
            throw new RuntimeException("대댓글 작성 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "수정할 댓글이나 대댓글 id와 변경하고싶은 내용을 받아, 댓글을 수정한다.")
    public void updateComment(
            String authorization,
            @ToolParam(description = "수정할 댓글이나 대댓글 id") Long commentId,
            @ToolParam(description = "변경하고자 하는 내용") String content
    ) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("content", content);

        try {
            restClient
                    .patch()
                    .uri("/{commentId}", commentId)
                    .header("Authorization", authorization)
                    .body(map)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("댓글 수정 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "삭제할 댓글이나 대댓글 id를 받아, 댓글을 삭제한다.")
    public void deleteComment(
            String authorization,
            @ToolParam(description = "삭제할 댓글이나 대댓글 id") Long commentId
    ) {
        try {
            restClient
                    .delete()
                    .uri("/{commentId}", commentId)
                    .header("Authorization", authorization)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new RuntimeException("댓글 삭제 실패: " + e.getMessage(), e);
        }
    }
}
