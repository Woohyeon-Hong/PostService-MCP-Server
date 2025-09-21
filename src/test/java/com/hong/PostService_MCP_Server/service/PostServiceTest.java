//package com.hong.PostService_MCP_Server.service;
//
//import com.hong.PostService_MCP_Server.dto.comment.CommentPage;
//import com.hong.PostService_MCP_Server.dto.post.Page;
//import com.hong.PostService_MCP_Server.dto.post.Page.PostSummaryResponse;
//import com.hong.PostService_MCP_Server.dto.post.PostDetailResponse;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.net.URI;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//
//@SpringBootTest
//class PostServiceTest {
//
//    @Autowired
//    PostService postService;
//
//    @Autowired
//    UserService userService;
//
////    @BeforeEach
////    void beforeEach() {
////        long num = Math.round(Math.random() * 1000);
////
////        String username = "testUser" + num;
////        String password = "testPassword" +num;
////        String nickname = "testNickname" + num;
////
////        userService.signUpWithoutEmail(username, password, nickname);
////        String authorization = userService.login(username, password);
////
////        for (int i = 1; i <= 5; i++) {
////            userService.writePost(authorization, i + "_" + username + "title", i + "_" + username + "content");
////        }
////    }
//
//    @Test
//    void getAllPosts() {
//        //given
//        List<PostSummaryResponse> original = postService.getAllPosts(0, 1000);
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
//        List<PostSummaryResponse> changed = postService.getAllPosts(0, 1000);
//
//        //then
//        assertThat(original.size() + 1).isEqualTo(changed.size());
//    }
//
//    @Test
//    void getPost() {
//        //given
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
//        String path = userService.writePost(authorization, title, content, null).getPath();
//
//        String[] blocks = path.split("/");
//        Long postId = Long.parseLong(blocks[blocks.length - 1]);
//
//        //when
//        PostDetailResponse post = postService.getPost(postId);
//
//        //then
//        assertThat(post.title()).isEqualTo(title);
//        assertThat(post.content()).isEqualTo(content);
//    }
//
//    @Test
//    void searchPostsWithWriterAndTitle() {
//        //given
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
//            userService.writePost(authorization, nickname + "'s " + "title" + i, nickname + "'s " + "content" + i, null);
//        }
//
//        //when
//        List<PostSummaryResponse> responses = postService.searchPostsWithWriterAndTitle(nickname, nickname);
//
//        //then
//        assertThat(responses.size()).isEqualTo(5);
//        assertThat(responses.get(0).writerNickname()).isEqualTo(nickname);
//    }
//
//    @Test
//    void searchPostsWithWriter() {
//        //given
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
//            userService.writePost(authorization, nickname + "'s " + "title" + i, nickname + "'s " + "content" + i, null);
//        }
//
//        //when
//        List<PostSummaryResponse> responses = postService.searchPostsWithWriter(nickname);
//
//        //then
//        assertThat(responses.size()).isEqualTo(5);
//        assertThat(responses.get(0).writerNickname()).isEqualTo(nickname);
//    }
//
//    @Test
//    void searchPostsWithTitle() {
//        //given
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
//            userService.writePost(authorization, nickname + "'s " + "title" + i, nickname + "'s " + "content" + i, null);
//        }
//
//        //when
//        List<PostSummaryResponse> responses = postService.searchPostsWithTitle(nickname + "'s title");
//
//        //then
//        assertThat(responses.size()).isEqualTo(5);
//        assertThat(responses.get(0).writerNickname()).isEqualTo(nickname);
//    }
//
//    @Test
//    void updatePostWithTitleAndContent() {
//        //given
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        String title = nickname + "'s title";
//        String content = nickname + "'s content";
//        String path = userService.writePost(authorization, title, content, null).getPath();
//
//        String[] blocks = path.split("/");
//        Long postId = Long.parseLong(blocks[blocks.length - 1]);
//
//        String nextTitle = nickname + "'s new title";
//        String nextContent = nickname + "'s new content";
//
//        //when
//        postService.updatePostWithTitleAndContent(authorization, postId, nextTitle, nextContent);
//
//        //then
//        PostDetailResponse post = postService.getPost(postId);
//        assertThat(post.title()).isEqualTo(nextTitle);
//        assertThat(post.content()).isEqualTo(nextContent);
//    }
//
//    @Test
//    void updatePostWithTitle() {
//        //given
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        String title = nickname + "'s title";
//        String content = nickname + "'s content";
//        String path = userService.writePost(authorization, title, content, null).getPath();
//
//        String[] blocks = path.split("/");
//        Long postId = Long.parseLong(blocks[blocks.length - 1]);
//
//        String nextTitle = nickname + "'s new title";
//
//        //when
//        postService.updatePostWithTitle(authorization, postId, nextTitle);
//
//        //then
//        PostDetailResponse post = postService.getPost(postId);
//        assertThat(post.title()).isEqualTo(nextTitle);
//    }
//
//    @Test
//    void updatePostWithContent() {
//        //given
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        String title = nickname + "'s title";
//        String content = nickname + "'s content";
//        String path = userService.writePost(authorization, title, content, null).getPath();
//
//        String[] blocks = path.split("/");
//        Long postId = Long.parseLong(blocks[blocks.length - 1]);
//
//        String nextContent = nickname + "'s new content";
//
//        //when
//        postService.updatePostWithContent(authorization, postId, nextContent);
//
//        //then
//        PostDetailResponse post = postService.getPost(postId);
//        assertThat(post.content()).isEqualTo(nextContent);
//    }
//
//    @Test
//    void deletePost() {
//        //given
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        String title = nickname + "'s title";
//        String content = nickname + "'s content";
//        String path = userService.writePost(authorization, title, content, null).getPath();
//
//        String[] blocks = path.split("/");
//        Long postId = Long.parseLong(blocks[blocks.length - 1]);
//
//        //when
//        postService.deletePost(authorization, postId);
//
//        //then
//        assertThatThrownBy(() -> postService.getPost(postId)).isInstanceOf(RuntimeException.class);
//    }
//
//    @Test
//    void writeComment() {
//        //given
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        String title = nickname + "'s title";
//        String content = nickname + "'s content";
//        String path = userService.writePost(authorization, title, content, null).getPath();
//
//        String[] blocks = path.split("/");
//        Long postId = Long.parseLong(blocks[blocks.length - 1]);
//
//        String commentContent = "comment_test";
//
//        //when
//        URI uri = postService.writeComment(authorization, postId, commentContent);
//
//        //then
//        assertThat(uri.getPath().toString()).startsWith("/v2/comments/");
//    }
//
//    @Test
//    void getCommentsOfPost() {
//        //given
//        long num = Math.round(Math.random() * 1000);
//
//        String username = "testUser" + num;
//        String password = "testPassword" +num;
//        String nickname = "testNickname" + num;
//
//        userService.signUpWithoutEmail(username, password, nickname);
//        String authorization = userService.login(username, password);
//
//        String title = nickname + "'s title";
//        String content = nickname + "'s content";
//        String path = userService.writePost(authorization, title, content, null).getPath();
//
//        String[] blocks = path.split("/");
//        Long postId = Long.parseLong(blocks[blocks.length - 1]);
//
//        String commentContent = "comment_test";
//        URI uri = postService.writeComment(authorization, postId, commentContent);
//
//        //when
//        List<CommentPage.CommentResponse> responses = postService.getCommentsOfPost(postId);
//
//        //then
//        assertThat(responses.size()).isEqualTo(1);
//    }
//}