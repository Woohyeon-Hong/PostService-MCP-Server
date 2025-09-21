package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.file.DownloadUrlResponse;
import com.hong.PostService_MCP_Server.dto.file.UploadUrlResponses;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

@Service
public class FileService {

    private final RestClient restClient;
    private final static String SERVICE_URL = "http://localhost:8080/v2/files";

    public FileService() {
        this.restClient = RestClient.builder()
                .baseUrl(SERVICE_URL)
                .defaultHeader("Origin", "http://localhost:8080")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Tool(description = "업로드 할 파일의 이름 리스트를 받아서, 해당 파일들의 업로드 용 presignedUrl을 발급한다.")
    public UploadUrlResponses getUploadPresignedUrl(
            String authorization,
            @ToolParam(description = "업로드 할 파일 이름들") List<String> originalFileNames
    ) {

        HashMap<String, List<String>> map = new HashMap<>();
        map.put("originalFileNames", originalFileNames);

        try {
            UploadUrlResponses responses = restClient
                    .post()
                    .uri("/upload-urls")
                    .header("Authorization", authorization)
                    .body(map)
                    .retrieve()
                    .body(UploadUrlResponses.class);

            return responses;
        } catch (RestClientException e) {
            throw new RuntimeException("업로드 url 발급 실패: " + e.getMessage(), e);
        }
    }

    @Tool(description = "다운 받을 파일의 id를 받아, 먼저 해당 파일의 다운로드 용 presignedUrl을 발급받고 그리고 발급받은 url로 파일을 다운받는다.")
    public File downloadFile(
            String authorization,
            @ToolParam(description = "첨부된 파일들을 다운받을 게시글 id") Long fileId
    ) {

        DownloadUrlResponse response = null;

        try {
            response = restClient
                    .post()
                    .uri("/{fileId}/download-url", fileId)
                    .header("Authorization", authorization)
                    .retrieve()
                    .body(DownloadUrlResponse.class);
        } catch (RestClientException e) {
            throw new RuntimeException("다운로드 url 발급 실패: " + e.getMessage(), e);
        }

        return RestClient
                .create()
                .get()
                .uri(URI.create(response.downloadUrl()))
                .retrieve()
                .body(File.class);
    }
}
