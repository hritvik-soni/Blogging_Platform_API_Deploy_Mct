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
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
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

    public PostResponse getPostbyId(Long postId) {

        if(postRepository.findById(postId).isPresent()){

            Optional<Post> getPost= postRepository.findById(postId);
            return PostResponse.builder()
                    .responseCode("010")
                    .responseMessage("Your Post")
                    .post(getPost)
                    .build();
        }
        return PostResponse.builder()
                .responseCode("011")
                .responseMessage( "Post not found for Post id: "+ postId)
                .post(Optional.empty())
                .build();

    }

    public PostResponse updatePost(String userName, String password,Long postId, String postTitle) throws NoSuchAlgorithmException {

        Post post  = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUserName(userName);

        if(user== null){
            return PostResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }
        if(post== null){
            return PostResponse.builder()
                    .responseCode("012")
                    .responseMessage("Post Doesn't NOt Exist")
                    .build();
        }
        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        boolean correctPass=encryptedPassword.equals(user.getPassword());

        if( correctPass && post.getUser().equals(user) ){

            post.setPostTitle(postTitle);
            postRepository.save(post);

            Optional<Post> newPost= Optional.of(post);

            return  PostResponse.builder()
                    .responseCode("013")
                    .responseMessage("Your Post Updated Successfully")
                    .post(newPost)
                    .build();

        }

        return PostResponse.builder()
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
