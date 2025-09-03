package com.hong.PostService_MCP_Server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private  UserService userService;

    @Test
    void signOut() {
        //given
        String username = "testUser";
        String password = "testPassowrd";

        userService.signUpWithoutEmail(username, password, "testNickname");
        String authorization = userService.login(username, password);

        //when
        userService.signOut(authorization);

        //then
        assertThatThrownBy(() -> userService.login(username, password))
                .isInstanceOf(RuntimeException.class);
    }
}