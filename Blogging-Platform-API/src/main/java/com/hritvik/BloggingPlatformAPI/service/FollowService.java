package com.hritvik.BloggingPlatformAPI.service;

import com.hritvik.BloggingPlatformAPI.model.Follow;
import com.hritvik.BloggingPlatformAPI.model.User;
import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.model.dto.FollowResponse;
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

    private List<Follow> allFollows = new ArrayList<>();
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


    public FollowResponse getAllFollowings(String userName, String password) throws NoSuchAlgorithmException {

        User existingUser = userRepository.findByUserName(userName);

        if(existingUser== null){
            return FollowResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())){

         List<String> userNameList = new ArrayList<String>();

            Set<String> following=new HashSet<>();
            for (Follow follow : allFollows) {
                if (Objects.equals(follow.getFollowerId().getUserId(), existingUser.getUserId())) {
                    following.add(follow.getFollowingId().getUserName());
                }
            }
            if(following.size()==0){
                return null;
            }
            return  FollowResponse.builder()
                    .responseCode("016")
                    .responseMessage("Your followings")
                    .followUserNames(new ArrayList<>(following))
                    .build();

        }

        return FollowResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();


    }

    public FollowResponse getAllFollowers(String userName, String password) throws NoSuchAlgorithmException {

        User existingUser = userRepository.findByUserName(userName);

        if(existingUser== null){
            return FollowResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())){

            Set<String> follower=new HashSet<>();
            for (Follow follow : allFollows) {
                if (Objects.equals(follow.getFollowingId().getUserId(), existingUser.getUserId())) {
                    follower.add(follow.getFollowerId().getUserName());
                }
            }
            if(follower.size()==0){
                return null;
            }

            return  FollowResponse.builder()
                    .responseCode("017")
                    .responseMessage("Your Followers")
                    .followUserNames(new ArrayList<>(follower))
                    .build();

        }

        return FollowResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();


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
