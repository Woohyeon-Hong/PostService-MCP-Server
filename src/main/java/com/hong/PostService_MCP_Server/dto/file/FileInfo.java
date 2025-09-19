package com.hong.PostService_MCP_Server.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileInfo(
        @JsonProperty("path") String path
) {
}







