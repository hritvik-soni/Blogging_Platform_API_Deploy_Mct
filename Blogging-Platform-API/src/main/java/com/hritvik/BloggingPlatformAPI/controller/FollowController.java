package com.hritvik.BloggingPlatformAPI.controller;

import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.model.dto.FollowResponse;
import com.hritvik.BloggingPlatformAPI.model.dto.PostRequest;
import com.hritvik.BloggingPlatformAPI.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("api/follow")
public class FollowController {

    @Autowired
    FollowService followService;

@PostMapping ("/create")
    public BlogResponse createFollow(@RequestParam String userName,
                                     @RequestParam String password ,
                                     @RequestParam String followUseName) throws NoSuchAlgorithmException {

    return followService.createFollow(userName,password,followUseName);

 }

 @GetMapping("/followings")
    public List<String> getAllFollowings(@RequestParam String userName,
                           @RequestParam String password ) throws NoSuchAlgorithmException {
    return followService.getAllFollowings(userName,password);
 }

    @GetMapping("/followers")
    public List<String> getAllFollowers(@RequestParam String userName,
                                        @RequestParam String password ) throws NoSuchAlgorithmException {
      return  followService.getAllFollowers(userName,password);
    }

    @DeleteMapping("/unfollow")
    public BlogResponse  deleteFollow(@RequestParam String userName,
                                     @RequestParam String password ,
                                     @RequestParam Long followId) throws NoSuchAlgorithmException {
    return followService.deleteFollow(userName,password, followId);
    }

}
