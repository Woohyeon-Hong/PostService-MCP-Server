package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.post.Page;
import com.hong.PostService_MCP_Server.dto.post.PostDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private  UserService userService;

    @Test
    void signOut() {
        //given
        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassowrd" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        //when
        userService.signOut(authorization);

        //then
        assertThatThrownBy(() -> userService.login(username, password))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void writePost() {
        //given
        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassowrd" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        String title = "title_test";
        String content = "content_test";

        //when
        userService.writePost(authorization, title, content);

        //then
        Page.PostSummaryResponse post = userService.getMemberPosts(authorization, 0, 1).get(0);
        assertThat(post.title()).isEqualTo(title);
        assertThat(post.title()).isEqualTo(title);
        assertThat(post.writerNickname()).isEqualTo(nickname);
        assertThat(post.createdDate()).isNotNull();
    }


}