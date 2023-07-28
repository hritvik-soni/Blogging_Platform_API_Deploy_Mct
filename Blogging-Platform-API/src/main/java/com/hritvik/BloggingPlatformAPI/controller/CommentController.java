package com.hritvik.BloggingPlatformAPI.controller;

import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/addComment/{userName}/{postId}/{comment}")
    public BlogResponse addComment(@PathVariable String userName,
                                   @PathVariable Long postId,
                                   @PathVariable String comment) {
        return commentService.addComment(userName, postId, comment);
    }

    @GetMapping("/getcomments/{userName}/{postId}")
    public List<String> allComments(@PathVariable String userName,
                                    @PathVariable Long postId) {
        return commentService.getComments(userName, postId);
    }

    @DeleteMapping("/delete")
    public BlogResponse deleteComment(@RequestParam String userName ,
                                   @RequestParam String password ,
                                   @RequestParam  Long commentId) throws NoSuchAlgorithmException {

        return  commentService.deleteComment(userName,password,commentId);
    }
}
