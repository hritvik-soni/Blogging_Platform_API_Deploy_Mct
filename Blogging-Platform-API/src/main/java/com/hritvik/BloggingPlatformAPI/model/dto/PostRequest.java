package com.hritvik.BloggingPlatformAPI.model.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PostRequest {

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String postTitle;

    @NotBlank(message = "Body is required")
    @Column(nullable = false, length = 10000)
    private String postBody;
}
