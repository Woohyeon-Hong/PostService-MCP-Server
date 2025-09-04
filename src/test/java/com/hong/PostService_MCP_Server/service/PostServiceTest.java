package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.post.Page;
import com.hong.PostService_MCP_Server.dto.post.PostDetailResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

//    @BeforeEach
//    void beforeEach() {
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        for (int i = 1; i <= 5; i++) {
//            userService.writePost(authorization, i + "_" + username + "title", i + "_" + username + "content");
//        }
//    }

    @Test
    void getAllPosts() {
        //given
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
        List<Page.PostSummaryResponse> changed = postService.getAllPosts(0, 1000);

        //then
        assertThat(original.size() + 1).isEqualTo(changed.size());
    }

    @Test
    void getPost() {
        //given
        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassword" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        String title = "test_title" + num;
        String content = "test_content" + num;
        String path = userService.writePost(authorization, title, content).getPath();

        String[] blocks = path.split("/");
        Long postId = Long.parseLong(blocks[blocks.length - 1]);

        //when
        PostDetailResponse post = postService.getPost(postId);

        //then
        assertThat(post.title()).isEqualTo(title);
        assertThat(post.content()).isEqualTo(content);
    }
}