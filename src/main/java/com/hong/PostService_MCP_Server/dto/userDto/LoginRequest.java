package com.hong.PostService_MCP_Server.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginRequest(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password
) {
}
