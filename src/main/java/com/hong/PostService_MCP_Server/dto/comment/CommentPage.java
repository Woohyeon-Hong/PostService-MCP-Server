package com.hong.PostService_MCP_Server.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentPage<T>(@JsonProperty("content") List<CommentResponse> content,
                             long totalElements,
                             int totalPages,
                             boolean last,
                             int size,
                             int number) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CommentResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("content") String content,
            @JsonProperty("writer") String writer,
            @JsonProperty("createdDate") LocalDateTime createdDate,
            @JsonProperty("parentCommentId") Long parentCommentId
    ) {
    }
}
