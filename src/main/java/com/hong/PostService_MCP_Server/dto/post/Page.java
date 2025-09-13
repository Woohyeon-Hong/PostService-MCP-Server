package com.hong.PostService_MCP_Server.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Page<T>( @JsonProperty("content") List<PostSummaryResponse> content,
                       long totalElements,
                       int totalPages,
                       boolean last,
                       int size,
                       int number) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PostSummaryResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("writerNickname") String writerNickname,
            @JsonProperty("createdDate") LocalDateTime createdDate,
            @JsonProperty("commentCount") int commentCount,
            @JsonProperty("includingFile") boolean includingFile
    ) {
    }
}
