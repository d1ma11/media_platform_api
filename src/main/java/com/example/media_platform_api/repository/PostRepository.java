package com.example.media_platform_api.repository;

import com.example.media_platform_api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
