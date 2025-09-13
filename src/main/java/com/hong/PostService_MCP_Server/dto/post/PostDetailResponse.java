package com.hong.PostService_MCP_Server.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PostDetailResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("title") String title,
        @JsonProperty("content") String content,
        @JsonProperty("writerNickname") String writerNickname,
        @JsonProperty("createdDate") LocalDateTime createdDate,
        @JsonProperty("lastModifiedDate") LocalDateTime lastModifiedDate,
        @JsonProperty("files") List<FileResponse> files
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FileResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("originalFileName") String originalFileName
    ) {
    }
}
