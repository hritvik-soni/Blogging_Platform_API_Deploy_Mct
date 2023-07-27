package com.hritvik.BloggingPlatformAPI.model.dto;

import com.hritvik.BloggingPlatformAPI.model.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FollowResponse {
    private String responseCode;
    private String responseMessage;
    private List<String> followUserNames;
}
