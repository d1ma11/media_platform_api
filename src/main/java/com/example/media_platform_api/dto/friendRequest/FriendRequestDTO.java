package com.example.media_platform_api.dto.friendRequest;

import lombok.Data;

@Data
public class FriendRequestDTO {
    private Long senderId;
    private Long receiverId;
}