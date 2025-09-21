# PostService MCP Server

## 프로젝트 소개

>PostService MCP Server는 
>my_first-toy-project-post-service (http://api.my-post-service.kro.kr)
>를 위한 Model Context Protocol (MCP) 서버입니다. 
>> -  post-service API를 통한 사용자 관리, 게시글 관리, 댓글 관리, 파일 관리 등의 기능을 LLM 모델이 사용할 수 있게 지원 합니다.
>>  my_first-toy-project-post-service github Repository 주소: https://github.com/Woohyeon-Hong/my_first-toy-project-post-service

## 주요 기술 스택

- **Spring Boot 3.5.5**: 웹 애플리케이션 프레임워크
- **Spring AI**: MCP 서버 구현
- **Gradle**: 빌드 도구

## Installation

### Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher

### 빌드하기

```bash
# repository를 포크하세요.
git clone <repository-url>
cd PostService-MCP-Server

# 프로젝트를 빌드하세요.
./gradlew build
```

### Cursor에 MCP 서버 추가하기

1. 먼저 JAR 파일을 빌드합니다:
```bash
./gradlew clean build
```

2. Cursor의 설정 파일 (`~/.cursor/mcp.json`)에 다음 내용을 추가합니다:

```json
{
  "mcpServers": {
    "postService-mcp-server": {
      "command": "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/bin/java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-Dspring.main.web-application-type=none",
        "-Dspring.main.banner-mode=off",
        "-Dlogging.level.root=OFF",
        "-Dorg.springframework.boot.logging.LoggingSystem=none",
        "-jar",
        "project_다운_directory/PostService-MCP-Server/build/libs/PostService-MCP-Server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

> **참고**: `jar_파일_절대경로` 부분을 실제 JAR 파일의 절대 경로로 변경해주세요.

3. Cursor를 재시작하면 MCP 서버가 연결됩니다.

### 성공 확인
- Cursor에서 MCP 서버가 연결되면 25개의 툴이 모두 인식됩니다.
- 채팅에서 "게시글을 작성해줘" 같은 요청을 하면 PostService 툴들이 사용 가능한 것을 확인할 수 있습니다.

## 사용 가능한 Tool 목록

### User 관리

#### 인증/인가
* **signUpWithoutEmail** - 일반 회원가입 (이메일 없이)
  * `username`: 가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가) (string, required)
  * `password`: 가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가) (string, required)
  * `nickname`: 가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가) (string, required)

* **signUpWithEmail** - 일반 회원가입 (이메일 포함)
  * `username`: 가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가) (string, required)
  * `password`: 가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가) (string, required)
  * `email`: 가입할 사용자의 이메일 주소 (이메일 주소 형식이어야 함, 기존 회원과 중복 불가) (string, required)
  * `nickname`: 가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가) (string, required)

* **signUpAdminWithoutEmail** - 어드민 회원가입 (이메일 없이)
  * `username`: 가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가) (string, required)
  * `password`: 가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가) (string, required)
  * `nickname`: 가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가) (string, required)

* **signUpAdminWithEmail** - 어드민 회원가입 (이메일 포함)
  * `username`: 가입할 사용자의 username (3-20자 사이, 기존 회원과 중복 불가) (string, required)
  * `password`: 가입할 사용자의 비밀번호 (6자 이상, 기존 회원과 중복 불가) (string, required)
  * `email`: 가입할 사용자의 이메일 주소 (이메일 주소 형식이어야 함, 기존 회원과 중복 불가) (string, required)
  * `nickname`: 가입할 사용자의 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가) (string, required)

* **login** - 사용자 로그인
  * `username`: 사용자 이름 (string, required)
  * `password`: 비밀번호 (string, required)

#### User 정보 관리
* **updateUsername** - 사용자명 수정
  * `authorization`: JWT 토큰 (string, required)
  * `username`: 변경할 사용자 이름 (3-20자 사이, 기존 회원과 중복 불가) (string, required)

* **updateEmail** - 이메일 수정
  * `authorization`: JWT 토큰 (string, required)
  * `email`: 변경할 email 주소 (이메일 주소 형식이어야 함, 기존 회원과 중복 불가) (string, required)

* **updateNickname** - 닉네임 수정
  * `authorization`: JWT 토큰 (string, required)
  * `nickname`: 변경할 닉네임 (빈 문자열 금지, 기존 회원과 중복 불가) (string, required)

* **updatePassword** - 비밀번호 수정
  * `authorization`: JWT 토큰 (string, required)
  * `currentPassword`: 기존 비밀번호 (string, required)
  * `newPassword`: 변경할 비밀번호 (6자 이상) (string, required)

* **signOut** - 회원 탈퇴
  * `authorization`: JWT 토큰 (string, required)

### 게시글 관리

#### 게시글 CRUD
* **writePost** - 게시글 작성
  * `authorization`: JWT 토큰 (string, required)
  * `title`: 작성할 게시글 제목 (string, required)
  * `content`: 작성할 게시글 본문 (string, required)
  * `fileInfoList`: 첨부할 파일들의 목록 (array, optional)

* **getAllPosts** - 전체 게시글 목록 조회
  * `page`: 조회할 페이지 번호 (0부터 시작) (number, required)
  * `size`: 한 페이지에 보여줄 게시글 수 (number, required)

* **getPost** - 게시글 상세 조회
  * `postId`: 게시글 id (number, required)

* **getMemberPosts** - 로그인한 회원의 게시글 목록 조회
  * `authorization`: JWT 토큰 (string, required)
  * `page`: 조회할 페이지 번호 (0부터 시작) (number, required)
  * `size`: 한 페이지에 보여줄 게시글 수 (number, required)

* **updatePostWithTitleAndContent** - 게시글 제목과 내용 수정
  * `authorization`: JWT 토큰 (string, required)
  * `postId`: 수정할 게시글 id (number, required)
  * `title`: 변경하고자 하는 제목 (string, required)
  * `content`: 변경하고자 하는 본문 (string, required)

* **updatePostWithTitle** - 게시글 제목만 수정
  * `authorization`: JWT 토큰 (string, required)
  * `postId`: 수정할 게시글 id (number, required)
  * `title`: 변경하고자 하는 제목 (string, required)

* **updatePostWithContent** - 게시글 내용만 수정
  * `authorization`: JWT 토큰 (string, required)
  * `postId`: 수정할 게시글 id (number, required)
  * `content`: 변경하고자 하는 본문 (string, required)

* **deletePost** - 게시글 삭제 (소프트 삭제)
  * `authorization`: JWT 토큰 (string, required)
  * `postId`: 삭제할 게시글 id (number, required)

#### 게시글 검색
* **searchPostsWithTitle** - 제목으로 게시글 검색
  * `title`: 게시글 제목 (string, required)

* **searchPostsWithWriter** - 작성자로 게시글 검색
  * `writer`: 게시글 작성자의 닉네임 (string, required)

* **searchPostsWithWriterAndTitle** - 작성자와 제목으로 게시글 검색
  * `writer`: 게시글 작성자의 닉네임 (string, required)
  * `title`: 게시글 제목 (string, required)

### 댓글 관리

* **writeComment** - 댓글 작성
  * `authorization`: JWT 토큰 (string, required)
  * `postId`: 댓글을 달 게시글 id (number, required)
  * `content`: 댓글을 달 내용 (string, required)

* **getCommentsOfPost** - 게시글의 댓글 목록 조회
  * `postId`: 댓글 목록을 조회할 게시글 id (number, required)

* **writeReply** - 대댓글 작성
  * `authorization`: JWT 토큰 (string, required)
  * `parentCommentId`: 대댓글을 작성할 댓글 id (number, required)
  * `postId`: 대댓글을 작성할 게시글 id (number, required)
  * `content`: 대댓글을 작성할 내용 (string, required)

* **updateComment** - 댓글 수정
  * `authorization`: JWT 토큰 (string, required)
  * `commentId`: 수정할 댓글이나 대댓글 id (number, required)
  * `content`: 변경하고자 하는 내용 (string, required)

* **deleteComment** - 댓글 삭제
  * `authorization`: JWT 토큰 (string, required)
  * `commentId`: 삭제할 댓글이나 대댓글 id (number, required)

### 파일 관리

* **getUploadPresignedUrl** - 파일 업로드용 presigned URL 발급
  * `authorization`: JWT 토큰 (string, required)
  * `originalFileNames`: 업로드 할 파일 이름들 (array, required)

* **downloadFile** - 파일 다운로드
  * `authorization`: JWT 토큰 (string, required)
  * `fileId`: 다운로드할 파일 id (number, required)

* **addAttachments** - 게시글에 파일 첨부
  * `authorization`: JWT 토큰 (string, required)
  * `postId`: 파일을 추가할 게시글 id (number, required)
  * `fileInfoList`: 추가하고자 하는 파일들의 dto 리스트 (array, required)

* **removeAttachments** - 게시글에서 파일 삭제
  * `authorization`: JWT 토큰 (string, required)
  * `postId`: 파일을 삭제할 게시글 id (number, required)
  * `fileIds`: 삭제하고자 하는 파일 id 목록 (array, required)

## 사용 예시

### 1. 사용자 회원가입 및 로그인
```
1. signUpWithoutEmail로 회원가입
2. login으로 로그인하여 JWT 토큰 획득
3. 획득한 토큰으로 다른 기능들 사용
```

### 2. 게시글 작성 및 관리
```
1. writePost로 게시글 작성
2. getAllPosts로 전체 게시글 목록 조회
3. searchPostsWithTitle로 특정 제목 검색
4. updatePostWithTitleAndContent로 게시글 수정
```

### 3. 댓글 시스템
```
1. writeComment로 댓글 작성
2. getCommentsOfPost로 댓글 목록 조회
3. writeReply로 대댓글 작성
4. updateComment로 댓글 수정
```

## 주의사항

- 모든 인증이 필요한 기능은 `authorization` 매개변수에 JWT 토큰을 포함해야 합니다.
- 파일 업로드 시 `fileInfoList`에는 실제 파일 경로를 포함해야 합니다.
- 게시글과 댓글은 소프트 삭제 방식으로 처리됩니다.
