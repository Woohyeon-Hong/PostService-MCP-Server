package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.post.Page;
import com.hong.PostService_MCP_Server.dto.post.Page.PostSummaryResponse;
import com.hong.PostService_MCP_Server.dto.post.PostDetailResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class PostService {

    private final RestClient restClient;
    private final static String SERVICE_URL = "http://localhost:8080/v2/posts";

    public PostService() {
        this.restClient = RestClient.builder()
                .baseUrl(SERVICE_URL)
                .defaultHeader("Origin", "http://localhost:8080")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
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
}
