package com.hong.PostService_MCP_Server.dto.comment;

public record ReplyCreateRequest(
        Long postId,
        String content
) {
}
