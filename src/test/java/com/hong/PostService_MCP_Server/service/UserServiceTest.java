// package com.hong.PostService_MCP_Server.service;
//
// import com.hong.PostService_MCP_Server.dto.file.FileInfo;
// import com.hong.PostService_MCP_Server.dto.file.UploadUrlResponses;
// import com.hong.PostService_MCP_Server.dto.file.UploadUrlResponses.UploadUrlResponse;
// import com.hong.PostService_MCP_Server.dto.post.Page;
// import com.hong.PostService_MCP_Server.dto.post.Page.PostSummaryResponse;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import java.io.File;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
// @SpringBootTest
// class UserServiceTest {
//
//     @Autowired
//     private UserService userService;
//
//     @Autowired
//     private FileService fileService;
//
//     @Test
//     void signOut() {
//         //given
//         long num = Math.round(Math.random() * 1000);
//
//         String username = "testUser" + num;
//         String password = "testPassowrd" +num;
//         String nickname = "testNickname" + num;
//
//         userService.signUpWithoutEmail(username, password, nickname);
//         String authorization = userService.login(username, password);
//
//         //when
//         userService.signOut(authorization);
//
//         //then
//         assertThatThrownBy(() -> userService.login(username, password))
//                 .isInstanceOf(RuntimeException.class);
//     }
//
//     @Test
//     void writePost_파일x() {
//         //given
//         long num = Math.round(Math.random() * 1000);
//
//         String username = "testUser" + num;
//         String password = "testPassowrd" +num;
//         String nickname = "testNickname" + num;
//
//         userService.signUpWithoutEmail(username, password, nickname);
//         String authorization = userService.login(username, password);
//
//         String title = "title_test";
//         String content = "content_test";
//
//         //when
//         userService.writePost(authorization, title, content, null);
//
//         //then
//         PostSummaryResponse post = userService.getMemberPosts(authorization, 0, 1).get(0);
//         assertThat(post.title()).isEqualTo(title);
//         assertThat(post.title()).isEqualTo(title);
//         assertThat(post.writerNickname()).isEqualTo(nickname);
//         assertThat(post.createdDate()).isNotNull();
//     }
//
//     @Test
//     void writePost_파일o() {
//         //given
//         long num = Math.round(Math.random() * 1000);
//
//         String username = "testUser" + num;
//         String password = "testPassowrd" +num;
//         String nickname = "testNickname" + num;
//
//         userService.signUpWithoutEmail(username, password, nickname);
//         String authorization = userService.login(username, password);
//
//         String title = "title_test";
//         String content = "content_test";
//
//         ArrayList<String> originalFileNames = new ArrayList<>();
//         originalFileNames.add("screen.png");
//
//         ArrayList<FileInfo> fileInfoList = new ArrayList<>();
//         fileInfoList.add(new FileInfo("/Users/hong-uhyeon/Desktop/STUDY/Develop/PostService-MCP-Server/src/test/java/com/hong/PostService_MCP_Server/service/screen.png"));
//
//         //when
//         userService.writePost(authorization, title, content, fileInfoList);
//
//         //then
//         PostSummaryResponse post = userService.getMemberPosts(authorization, 0, 1).get(0);
//
//         assertThat(post.includingFile()).isTrue();
//     }
// }