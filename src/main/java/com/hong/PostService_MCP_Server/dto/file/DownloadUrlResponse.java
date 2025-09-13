package com.hong.PostService_MCP_Server.dto.file;

import java.time.Instant;

public record DownloadUrlResponse(
        String downloadUrl,
        Instant expiresAt
) {
}
