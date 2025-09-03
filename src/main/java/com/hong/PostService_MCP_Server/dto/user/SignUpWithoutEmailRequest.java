package com.hong.PostService_MCP_Server.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties (ignoreUnknown = true)
public record SignUpWithoutEmailRequest(
    @JsonProperty("username") String username, 
    @JsonProperty("password") String password,
    @JsonProperty("nickname") String nickname) {
}
