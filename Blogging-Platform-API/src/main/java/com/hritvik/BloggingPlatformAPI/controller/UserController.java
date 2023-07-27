package com.hritvik.BloggingPlatformAPI.controller;


import com.hritvik.BloggingPlatformAPI.model.User;
import com.hritvik.BloggingPlatformAPI.model.dto.UserRequest;
import com.hritvik.BloggingPlatformAPI.model.dto.BlogResponse;
import com.hritvik.BloggingPlatformAPI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/{userId}")
    public Optional<User> getUser(@PathVariable Long userId) {

        return userService.getUserbyId(userId);
    }

    @GetMapping("All")

    public List<User> getAllUsers(){
            return userService.getAllUsers();
        }


    @PostMapping("/create")
    public BlogResponse createUser(@Valid @RequestBody UserRequest request) throws NoSuchAlgorithmException {

        return userService.createUser(request);
    }

    @DeleteMapping("/delete")
    public BlogResponse deleteUser(@RequestParam String userName ,
                                   @RequestParam String password ,
                                   @RequestParam  boolean sure_OR_not ) throws NoSuchAlgorithmException {

        return  userService.deleteUser(userName,password,sure_OR_not);
    }

    @PutMapping("/updateusercontact")
    public BlogResponse updateUser(@RequestParam String userName ,
                                   @RequestParam String userPassword ,
                                   @RequestParam String userContact ) throws NoSuchAlgorithmException {

        return userService.updateUser(userName,userPassword,userContact);
    }

}