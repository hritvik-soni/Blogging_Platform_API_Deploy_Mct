package com.hritvik.BloggingPlatformAPI.controller;

import com.hritvik.BloggingPlatformAPI.model.Post;
import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.model.dto.PostRequest;
import com.hritvik.BloggingPlatformAPI.model.dto.PostResponse;
import com.hritvik.BloggingPlatformAPI.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public BlogResponse createPost(@RequestBody PostRequest request, @RequestParam String userName, @RequestParam String password) throws NoSuchAlgorithmException {
      return postService.createPost(request,userName,password);
    }

    @GetMapping("/getAllPostsOfUser")
    public ResponseEntity<List<String>> getPostsByUserId(@RequestParam String  userName) {

        return postService.getPostsByUserId(userName);
    }

    @PutMapping("/update")
    public BlogResponse updatePost(  @RequestParam String userName,
                                     @RequestParam String password ,
                                     @RequestParam Long postId,
                                     @RequestParam String postTitle) throws NoSuchAlgorithmException {

        return postService.updatePost(userName,password,postId,postTitle);
    }

    @DeleteMapping("/delete")
    public BlogResponse deleteUser(@RequestParam String userName ,
                                   @RequestParam String password ,
                                   @RequestParam  Long postId) throws NoSuchAlgorithmException {

        return  postService.deletePost(userName,password,postId);
    }




}
