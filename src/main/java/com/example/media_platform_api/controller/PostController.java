package com.example.media_platform_api.controller;

import com.example.media_platform_api.dto.post.PostRequestDTO;
import com.example.media_platform_api.dto.post.PostResponseDTO;
import com.example.media_platform_api.dto.post.PostUpdateRequest;
import com.example.media_platform_api.model.Post;
import com.example.media_platform_api.model.User;
import com.example.media_platform_api.service.PostService;
import com.example.media_platform_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO postRequestDTO, Principal principal) {
        User currentUser = userService.findUserByUserName(principal.getName());
        Post newPost = postService.createPost(postRequestDTO, currentUser.getId());
        PostResponseDTO postResponseDTO = convertToPostResponseDTO(newPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        PostResponseDTO postResponseDTO = convertToPostResponseDTO(post);
        return ResponseEntity.ok(postResponseDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId, @Valid @RequestBody PostUpdateRequest postUpdateRequest) {
        Post updatedPost = postService.updatePost(postId, postUpdateRequest);
        PostResponseDTO postResponseDTO = convertToPostResponseDTO(updatedPost);
        return ResponseEntity.ok(postResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    private PostResponseDTO convertToPostResponseDTO(Post post) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(post.getId());
        postResponseDTO.setTitle(post.getTitle());
        postResponseDTO.setText(post.getContent());
        postResponseDTO.setSendDate(post.getCreatedAt());

        return postResponseDTO;
    }
}