package com.hong.PostService_MCP_Server.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PostCreateRequest(
        @JsonProperty("title") String title,
        @JsonProperty("content") String content
) {
}
