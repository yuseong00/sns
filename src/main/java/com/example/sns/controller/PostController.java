package com.example.sns.controller;

import com.example.sns.controller.response.Response;
import com.example.sns.model.Post;
import com.example.sns.model.User;
import com.example.sns.service.PostService;
import com.example.sns.util.ClassUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import static com.example.sns.controller.UserController.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor

public class PostController {


    private final PostService postService;


    @PostMapping
    public Response<Void> create(@RequestBody PostWriteRequest request, Authentication authentication) {
        postService.create(authentication.getName(), request.getTitle(), request.getBody());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        return Response.success(PostResponse.fromPost(
                        postService.modify(user.getId(), postId, request.getTitle(), request.getBody())));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        postService.delete(user.getId(), postId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> myPosts(Pageable pageable, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        return Response.success(postService.my(user.getId(), pageable).map(PostResponse::fromPost));
    }
    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }




    @Data
    @AllArgsConstructor
    public static class PostWriteRequest {
        private String title;
        private String body;
    }


    @Data
    @AllArgsConstructor
    public static class PostModifyRequest {
        private String title;
        private String body;
    }


    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private Integer id;
        private String title;
        private String body;
        private UserResponse user;
        private Timestamp registeredAt;
        private Timestamp updatedAt;

        public static PostResponse fromPost(Post post) {
            return new PostResponse(
                    post.getId(),
                    post.getTitle(),
                    post.getBody(),
                    UserResponse.fromUser(post.getUser()),
                    post.getRegisteredAt(),
                    post.getUpdatedAt()
            );
        }

    }
}
