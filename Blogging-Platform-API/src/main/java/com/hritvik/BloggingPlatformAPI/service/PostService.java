package com.hritvik.BloggingPlatformAPI.service;

import com.hritvik.BloggingPlatformAPI.model.Post;
import com.hritvik.BloggingPlatformAPI.model.User;
import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.model.dto.PostRequest;
import com.hritvik.BloggingPlatformAPI.model.dto.PostResponse;
import com.hritvik.BloggingPlatformAPI.repository.IPostRepository;
import com.hritvik.BloggingPlatformAPI.repository.IUserRepository;
import com.hritvik.BloggingPlatformAPI.service.utility.AccountUtils;
import com.hritvik.BloggingPlatformAPI.service.utility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class PostService {

    @Autowired
    IPostRepository postRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    UserService userService;

    public BlogResponse createPost(PostRequest request, String userName, String password) throws NoSuchAlgorithmException {

        User existingUser = userRepository.findByUserName(userName);
        if(existingUser== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())){

            Post newPost= Post.builder()
                    .postTitle(request.getPostTitle())
                    .postBody(request.getPostBody())
                    .user(existingUser)
                    .build();
            postRepository.save(newPost);

            return  BlogResponse.builder()
                    .responseCode("009")
                    .responseMessage("Your Post Created Successfully")
                    .build();

        }

        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }
    public List<Post> getAllPostOfUser(Long userId) {
        List<Post> existingPosts = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            if (Objects.equals(post.getUser().getUserId(), userId)) {
                existingPosts.add(post);
            }
        }
        if (existingPosts.size() == 0) {
            return null;
        }
        return existingPosts.stream().toList();
    }


    public ResponseEntity<List<String>> getPostsByUserId(String userName) {

        User existingUser = userRepository.findByUserName(userName);
        Long userId= existingUser.getUserId();

        if (!userRepository.findById(userId).isPresent()) {
            return new ResponseEntity<>(List.of("User with userId " + userId + " doesn't exist"), HttpStatus.NOT_FOUND);
        }



        if (getAllPostOfUser(userId) == null) {
            return new ResponseEntity<>(List.of("User with userId " + userId + " haven't posted any post"), HttpStatus.FOUND);
        }

        List<Post> allPosts = postRepository.findAll();
        List<String> postBody = new ArrayList<>();
        for (Post post : allPosts) {
            if(post.getUser().getUserId().equals(userId)) {
                postBody.add(post.getPostBody());
            }
        }
        return new ResponseEntity<>(postBody, HttpStatus.FOUND);
    }


    public BlogResponse updatePost(String userName, String password,Long postId, String postTitle) throws NoSuchAlgorithmException {

        Post post  = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUserName(userName);

        if(user== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)

                    .build();
        }
        if(post== null){
            return BlogResponse.builder()
                    .responseCode("012")
                    .responseMessage("Post Doesn't NOt Exist")
                    .build();
        }
        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        boolean correctPass=encryptedPassword.equals(user.getPassword());

        if( correctPass && post.getUser().equals(user) ){

            post.setPostTitle(postTitle);
            postRepository.save(post);

            return  BlogResponse.builder()
                    .responseCode("013")
                    .responseMessage("Your Post Updated Successfully")
                    .build();

        }

        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }


    public BlogResponse deletePost(String userName, String password, Long postId) throws NoSuchAlgorithmException {

        Post post  = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUserName(userName);

        if(user== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }
        if(post== null){
            return BlogResponse.builder()
                    .responseCode("012")
                    .responseMessage("Post Doesn't NOt Exist")
                    .build();
        }
        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        boolean correctPass=encryptedPassword.equals(user.getPassword());

        if( correctPass && post.getUser().equals(user) ){

            postRepository.deleteById(postId);

            return  BlogResponse.builder()
                    .responseCode("014")
                    .responseMessage("Your Post Deleted Successfully")
                    .build();

        }

        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }

    public List<Post> getAllPost() {
       return  postRepository.findAll();
    }
}
