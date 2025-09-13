package com.hong.PostService_MCP_Server.dto.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UploadUrlResponses(
        @JsonProperty("results") List<UploadUrlResponse> results
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UploadUrlResponse(
            @JsonProperty("originalFileName") String originalFileName,
            @JsonProperty("s3Key") String s3Key,
            @JsonProperty("storedFileName") String storedFileName,
            @JsonProperty("uploadUrl") String uploadUrl,
            @JsonProperty("expiresAt") LocalDateTime expiresAt
    ) {
    }
}
