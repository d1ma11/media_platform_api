package com.example.media_platform_api.dto.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String text;
    private LocalDateTime sendDate;
}
