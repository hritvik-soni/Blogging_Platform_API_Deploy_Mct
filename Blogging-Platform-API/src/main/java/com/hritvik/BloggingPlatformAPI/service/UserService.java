package com.hritvik.BloggingPlatformAPI.service;


import com.hritvik.BloggingPlatformAPI.model.User;
import com.hritvik.BloggingPlatformAPI.model.dto.UserRequest;
import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.repository.IUserRepository;
import com.hritvik.BloggingPlatformAPI.service.utility.AccountUtils;
import com.hritvik.BloggingPlatformAPI.service.utility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;


    public Optional<User> getUserbyId(Long userId) {
        return userRepository.findById(userId);
    }

    public BlogResponse createUser(UserRequest userRequest) throws NoSuchAlgorithmException {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)

                    .build();
        }
        String encryptedPassword = PasswordEncrypter.encryptPassword(userRequest.getPassword());

        User newUser = User.builder()
                .userName(userRequest.getUserName())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .dateOfBirth(userRequest.getDateOfBirth())
                .gender(userRequest.getGender())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(encryptedPassword)
                .build();

        User savedUser = userRepository.save(newUser);

        return  BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .build();
    }

    public BlogResponse deleteUser(String userName, String password, boolean sureOrNot) throws NoSuchAlgorithmException {

        if(!sureOrNot){
            return  BlogResponse.builder()
                    .responseCode("006")
                    .responseMessage("Thanks for Not deleting Account")
                    .build();
        }

        User existingUser = userRepository.findByUserName(userName);
        if(existingUser== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(password);

        if(encryptedPassword.equals(existingUser.getPassword())){
            Long userid= existingUser.getUserId();
            userRepository.deleteById(userid);

            return  BlogResponse.builder()
                    .responseCode("007")
                    .responseMessage("Your Account DELETED Successfully")
                    .build();

        }

        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }

    public BlogResponse updateUser(String userName, String userPassword, String userContact) throws NoSuchAlgorithmException {


        User existingUser = userRepository.findByUserName(userName);
        if(existingUser== null){
            return BlogResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        String encryptedPassword = PasswordEncrypter.encryptPassword(userPassword);

        if(encryptedPassword.equals(existingUser.getPassword())){

            existingUser.setPhoneNumber(userContact);
            userRepository.save(existingUser);

            return  BlogResponse.builder()
                    .responseCode("008")
                    .responseMessage("Your Account contact no. Update Successfully")
                    .build();

        }

        return BlogResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_INVALID_CREDENTIALS_MESSAGE)
                .build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}