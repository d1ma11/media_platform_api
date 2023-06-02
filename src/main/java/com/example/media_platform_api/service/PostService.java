package com.example.media_platform_api.service;

import com.example.media_platform_api.dto.post.PostRequestDTO;
import com.example.media_platform_api.dto.post.PostUpdateRequest;
import com.example.media_platform_api.model.Post;
import com.example.media_platform_api.model.User;
import com.example.media_platform_api.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post createPost(PostRequestDTO postRequestDTO, Long userId) {
        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getText());
        post.setCreatedAt(LocalDateTime.now());
        User user = new User();
        user.setId(userId);
        post.setOwner(user);
        return postRepository.save(post);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Post not found with ID: " + postId));
    }

    public Post updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = getPostById(postId);
        post.setTitle(postUpdateRequest.getTitle());
        post.setContent(postUpdateRequest.getText());
        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        postRepository.delete(post);
    }
}
