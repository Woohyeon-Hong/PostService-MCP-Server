package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.file.UploadUrlResponses;
import com.hong.PostService_MCP_Server.dto.post.Page;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FileServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    FileService fileService;

    @Test
    void getUploadPresignedUrl() {
        //given
        List<String> originalFileNames = new ArrayList<>();

        originalFileNames.add("file1.txt");
        originalFileNames.add("file2.txt");
        originalFileNames.add("file3.txt");
        originalFileNames.add("file4.txt");

        List<Page.PostSummaryResponse> original = postService.getAllPosts(0, 1000);

        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassword" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        String title = "test_title" + num;
        String content = "test_content" + num;
        userService.writePost(authorization, title, content);

        //when
        UploadUrlResponses responses = fileService.getUploadPresignedUrl(authorization, originalFileNames);

        //then

        assertThat(responses.results().size()).isEqualTo(4);

    }
}