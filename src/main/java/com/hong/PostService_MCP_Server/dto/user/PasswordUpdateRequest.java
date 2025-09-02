package com.hong.PostService_MCP_Server.dto.user;

public record PasswordUpdateRequest(
        String currentPassword,
        String newPassword
) {
}
