package com.hong.PostService_MCP_Server.service;

import com.hong.PostService_MCP_Server.dto.comment.CommentPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommentServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Test
    void writeReply() {
        //given
        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassword" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        String title = nickname + "'s title";
        String content = nickname + "'s content";
        String path = userService.writePost(authorization, title, content).getPath();

        String[] blocks = path.split("/");
        Long postId = Long.parseLong(blocks[blocks.length - 1]);

        String commentContent = "comment_test";

        path = postService.writeComment(authorization, postId, commentContent).getPath();
        blocks = path.split("/");
        Long commentId = Long.parseLong(blocks[blocks.length - 1]);

        String relplyContent = "reply_test";

        //when
        URI uri = commentService.writeReply(authorization, commentId, postId, relplyContent);

        //then
        assertThat(uri.getPath().toString()).startsWith("/v2/comments/");
    }

    @Test
    void updateComment_부모댓글만() {
        //given
        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassword" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        String title = nickname + "'s title";
        String content = nickname + "'s content";
        String path = userService.writePost(authorization, title, content).getPath();

        String[] blocks = path.split("/");
        Long postId = Long.parseLong(blocks[blocks.length - 1]);

        String commentContent = "comment_test";
        path = postService.writeComment(authorization, postId, commentContent).getPath();
        blocks = path.split("/");
        Long commentId = Long.parseLong(blocks[blocks.length - 1]);

        String newContent = "updated_comment";

        //when
        commentService.updateComment(authorization, commentId, newContent);

        //then
        List<CommentPage.CommentResponse> responses = postService.getCommentsOfPost(postId);
        for (CommentPage.CommentResponse respons : responses) {
            System.out.println(respons);
        }
    }

    @Test
    void updateComment_대댓글만() {
        //given
        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassword" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        String title = nickname + "'s title";
        String content = nickname + "'s content";
        String path = userService.writePost(authorization, title, content).getPath();

        String[] blocks = path.split("/");
        Long postId = Long.parseLong(blocks[blocks.length - 1]);

        String commentContent = "comment_test";
        path = postService.writeComment(authorization, postId, commentContent).getPath();
        blocks = path.split("/");
        Long commentId = Long.parseLong(blocks[blocks.length - 1]);

        String replyContent = "reply_test";
        path = commentService.writeReply(authorization, commentId, postId, replyContent).getPath();
        blocks = path.split("/");
        Long replyId = Long.parseLong(blocks[blocks.length - 1]);

        String newContent = "updated_reply";

        //when
        commentService.updateComment(authorization, replyId, newContent);

        //then
        List<CommentPage.CommentResponse> responses = postService.getCommentsOfPost(postId);
        for (CommentPage.CommentResponse respons : responses) {
            System.out.println(respons);
        }
    }

    @Test
    void deleteComment() {
        //given
        long num = Math.round(Math.random() * 1000);

        String username = "testUser" + num;
        String password = "testPassword" +num;
        String nickname = "testNickname" + num;

        userService.signUpWithoutEmail(username, password, nickname);
        String authorization = userService.login(username, password);

        String title = nickname + "'s title";
        String content = nickname + "'s content";
        String path = userService.writePost(authorization, title, content).getPath();

        String[] blocks = path.split("/");
        Long postId = Long.parseLong(blocks[blocks.length - 1]);

        String commentContent = "comment_test";

        path = postService.writeComment(authorization, postId, commentContent).getPath();
        blocks = path.split("/");
        Long commentId = Long.parseLong(blocks[blocks.length - 1]);

        //when
        commentService.deleteComment(authorization, commentId);

        //then
        List<CommentPage.CommentResponse> responses = postService.getCommentsOfPost(postId);
        assertThat(responses.size()).isEqualTo(0);
    }
}