package com.hong.PostService_MCP_Server.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties (ignoreUnknown = true)
public record SignUpRequest(
    @JsonProperty("username") String username, 
    @JsonProperty("password") String password, 
    @JsonProperty("email") String email, 
    @JsonProperty("nickname") String nickname) {
}
