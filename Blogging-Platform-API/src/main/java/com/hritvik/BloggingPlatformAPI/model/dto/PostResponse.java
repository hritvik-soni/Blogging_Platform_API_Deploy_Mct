package com.hritvik.BloggingPlatformAPI.model.dto;

import com.hritvik.BloggingPlatformAPI.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private String responseCode;
    private String responseMessage;
    private Optional<Post> post;
}
