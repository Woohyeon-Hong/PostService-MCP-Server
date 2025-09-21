package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.file.DownloadUrlResponse;
import com.hong.PostService_MCP_Server.dto.file.UploadUrlResponses;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Tool(description = "다운 받을 파일의 id를 받아, 먼저 해당 파일의 다운로드 용 presignedUrl을 발급받고 그리고 발급받은 url로 파일을 home/downloads에 다운받는다.")
    public File downloadFile(
            String authorization,
            @ToolParam(description = "첨부된 파일들을 다운받을 게시글 id") Long fileId
    ) {

        try {
            DownloadUrlResponse downloadUrlResponse = getDownloadUrlResponse(authorization, fileId);
            ResponseEntity<Resource> fileResource = getFileResource(downloadUrlResponse);

            File downloadedFile = placeFileOnDownloads(fileResource);

            return downloadedFile;
        } catch (RestClientException e) {
            throw new RuntimeException("파일 다운로드 요청 실패: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    private static ResponseEntity<Resource> getFileResource(DownloadUrlResponse downloadUrlResponse) {
        ResponseEntity<Resource> response = RestClient.create()
                .get()
                .uri(URI.create(downloadUrlResponse.downloadUrl()))
                .retrieve()
                .toEntity(Resource.class);
        return response;
    }

    private DownloadUrlResponse getDownloadUrlResponse(String authorization, Long fileId) {
        DownloadUrlResponse downloadUrlResponse = restClient
                .post()
                .uri("/{fileId}/download-url", fileId)
                .header("Authorization", authorization)
                .retrieve()
                .body(DownloadUrlResponse.class);
        return downloadUrlResponse;
    }

    private File placeFileOnDownloads(ResponseEntity<Resource> response) throws IOException {
        File downloadedFile = createFile(response);

        try (InputStream is = response.getBody().getInputStream();
             FileOutputStream os = new FileOutputStream(downloadedFile)) {
            is.transferTo(os);
        }
        ;
        return downloadedFile;
    }

    private File createFile(ResponseEntity<Resource> response) throws IOException {
        Path downloadDir = createDownloadDirectory();
        String filename = createFilenameFrom(response.getHeaders());

        File downloadedFile = downloadDir.resolve(filename).toFile();
        return downloadedFile;
    }

    private static Path createDownloadDirectory() throws IOException {
        Path downloadDir = Paths.get(System.getProperty("user.home"), "Downloads");
        Files.createDirectories(downloadDir);
        return downloadDir;
    }

    private String createFilenameFrom(HttpHeaders headers) {
        String contentDisposition = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);

        if (contentDisposition == null)  return UUID.randomUUID().toString();

        int idx = contentDisposition.lastIndexOf(".");
        String format = contentDisposition.substring(idx + 1);

        return UUID.randomUUID() + "." + format;
    }
}
