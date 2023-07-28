package com.hritvik.BloggingPlatformAPI.service;

import com.hritvik.BloggingPlatformAPI.model.Follow;
import com.hritvik.BloggingPlatformAPI.model.User;
import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;

import com.hritvik.BloggingPlatformAPI.repository.IFollowRepository;
import com.hritvik.BloggingPlatformAPI.repository.IUserRepository;
import com.hritvik.BloggingPlatformAPI.service.utility.AccountUtils;
import com.hritvik.BloggingPlatformAPI.service.utility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class FollowService {

    @Autowired
    IFollowRepository followRepository;
    @Autowired
    UserService userService;
    @Autowired
    IUserRepository userRepository;

    public BlogResponse createFollow(String userName, String password, String followUseName) throws NoSuchAlgorithmException {

        User followUser = userRepository.findByUserName(followUseName);

        User existingUser = userRepository.findByUserName(userName);

        if(existingUser== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        if(followUser== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())){

            Follow newFollow = Follow.builder()
                    .followingId(followUser)
                    .followerId(existingUser)
                    .build();

            followRepository.save(newFollow);

            return  BlogResponse.builder()
                    .responseCode("015")
                    .responseMessage(userName +" is started following " + followUseName)
                    .build();

        }

        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }


    public List<String> getAllFollowings(String userName, String password) throws NoSuchAlgorithmException {

        User existingUser = userRepository.findByUserName(userName);

        if(existingUser== null){
            return List.of("User Not Found!!!!!");
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())){

            Set<String> following=new HashSet<>();
            for (Follow follow : followRepository.findAll()) {
                if (follow.getFollowerId().getUserId().equals(existingUser.getUserId())) {
                    following.add(follow.getFollowingId().getUserName());
                }
            }
            if(following.size()==0){
                return List.of(userName +"  You are not Following any one " );
            }
            return new ArrayList<>(following);
        }

        return List.of("Invalid Credentials!!!!!!!!!!");


    }

    public List<String> getAllFollowers(String userName, String password) throws NoSuchAlgorithmException {

        User existingUser = userRepository.findByUserName(userName);

        if(existingUser== null){
            return List.of("User Not Found!!!!!");
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())) {

            Set<String> follower = new HashSet<>();
            for (Follow follow : followRepository.findAll()) {
                if (follow.getFollowingId().getUserId().equals( existingUser.getUserId())) {
                    follower.add(follow.getFollowerId().getUserName());
                }
            }
            if (follower.size() == 0) {
                return List.of("No Followers for this user: " + userName);
            }
            return new ArrayList<>(follower);
        }
      return List.of("Invalid Credentials!!!!!!!!!!");
    }

    public Follow findFollow(Long followId) {
        return followRepository.findById(followId).orElse(null);
    }

    private boolean authorizeUnfollow(String email, Follow follow) {

        String  targetEmail = follow.getFollowerId().getEmail();
        String  followerEmail  = follow.getFollowingId().getEmail();;

        return targetEmail.equals(email) || followerEmail.equals(email);
    }

    public BlogResponse deleteFollow(String userName, String password, Long followId) throws NoSuchAlgorithmException {

        User existingUser = userRepository.findByUserName(userName);

        if(existingUser== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())){

            Follow follow  = findFollow(followId);
          if(follow != null)
         {
            if(authorizeUnfollow(existingUser.getEmail(), follow))
            {
                followRepository.delete(follow);
                return BlogResponse.builder()
                        .responseCode("018")
                        .responseMessage("Your is Unfollowed Successfully")
                        .build();
            }
         }
        }
        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }
}

