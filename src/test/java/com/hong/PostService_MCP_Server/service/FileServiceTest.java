package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.file.FileInfo;
import com.hong.PostService_MCP_Server.dto.file.UploadUrlResponses;
import com.hong.PostService_MCP_Server.dto.post.Page;
import com.hong.PostService_MCP_Server.dto.post.PostDetailResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URI;
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

//    @Test
//    void getUploadPresignedUrl() {
//        //given
//        List<String> originalFileNames = new ArrayList<>();
//
//        originalFileNames.add("file1.txt");
//        originalFileNames.add("file2.txt");
//        originalFileNames.add("file3.txt");
//        originalFileNames.add("file4.txt");
//
//        List<Page.PostSummaryResponse> original = postService.getAllPosts(0, 1000);
//
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        String title = "test_title" + num;
//        String content = "test_content" + num;
//        userService.writePost(authorization, title, content, null);
//
//        //when
//        UploadUrlResponses responses = fileService.getUploadPresignedUrl(authorization, originalFileNames);
//
//        //then
//
//        assertThat(responses.results().size()).isEqualTo(4);
//
//    }

    @Test
     void downloadFile() {
         //given
         long num = Math.round(Math.random() * 1000);

         String username = "testUser" + num;
         String password = "testPassowrd" +num;
         String nickname = "testNickname" + num;

         userService.signUpWithoutEmail(username, password, nickname);
         String authorization = userService.login(username, password);

         String title = "title_test";
         String content = "content_test";

         ArrayList<String> originalFileNames = new ArrayList<>();
         originalFileNames.add("screen.png");

         ArrayList<FileInfo> fileInfoList = new ArrayList<>();
         fileInfoList.add(new FileInfo("/Users/hong-uhyeon/Desktop/STUDY/Develop/PostService-MCP-Server/src/test/java/com/hong/PostService_MCP_Server/service/screen.png"));

        String path = userService.writePost(authorization, title, content, fileInfoList).getPath();

        String[] blocks = path.split("/");
        Long postId = Long.parseLong(blocks[blocks.length - 1]);

        PostDetailResponse post = postService.getPost(postId);

        System.out.println(post);

        Long fileId = post.files().get(0).id();

        //when
        File downloadFile = fileService.downloadFile(authorization, fileId);

        //then
        System.out.println("name : " + downloadFile.getName());
    }
}