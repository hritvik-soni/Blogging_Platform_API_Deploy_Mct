package com.hritvik.BloggingPlatformAPI.service;

import com.hritvik.BloggingPlatformAPI.model.Comment;
import com.hritvik.BloggingPlatformAPI.model.User;
import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.repository.ICommentRepository;
import com.hritvik.BloggingPlatformAPI.repository.IPostRepository;
import com.hritvik.BloggingPlatformAPI.repository.IUserRepository;
import com.hritvik.BloggingPlatformAPI.service.utility.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    @Autowired
    private ICommentRepository commentRepository;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;

    public BlogResponse addComment(String userName, Long postId, String reqCom) {

        User existingUser = userRepository.findByUserName(userName);

        if(existingUser== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        Long userId = existingUser.getUserId();
        Comment comment = new Comment();
        if (userRepository.findById(userId).isPresent() && postRepository.findById(postId).isPresent()) {
            Comment newComment = Comment.builder()
                    .commentBody(reqCom)
                    .user(existingUser)
                    .post(postRepository.findById(postId).get())
                    .build();

            commentRepository.save(newComment);

            return BlogResponse.builder()
                    .responseCode("019")
                    .responseMessage("Comment Added Successfully")
                    .build();

        }
        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }


    public List<String> getComments(String userName, Long postId) {

        User existingUser = userRepository.findByUserName(userName);
        Long userId = existingUser.getUserId();


        if(existingUser== null){
            return List.of("Invalid User");
        }

        List<String> commentBodies = new ArrayList<>();
        List<Comment> allComments = commentRepository.findAll();

        if (userRepository.findById(userId).isPresent() && postRepository.findById(postId).isPresent()) {

            for (Comment comment : allComments) {
                if (Objects.equals(comment.getPost().getPostId(), postId)) {
                    commentBodies.add(comment.getCommentBody());
                }
            }
            if (commentBodies.isEmpty()) {
                return List.of("No Comments on this Post");
            }

        }
        return commentBodies;
    }

}