package com.hong.PostService_MCP_Server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.hong.PostService_MCP_Server.service.PostService;
import com.hong.PostService_MCP_Server.service.UserService;

@SpringBootApplication
public class PostServiceMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostServiceMcpServerApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider tools(UserService userService, PostService postService) {
		return MethodToolCallbackProvider.builder().toolObjects(userService, postService).build();
	}

}
