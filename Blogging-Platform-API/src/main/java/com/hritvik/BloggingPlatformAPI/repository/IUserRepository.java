package com.hritvik.BloggingPlatformAPI.repository;


import com.hritvik.BloggingPlatformAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Long> {
    User findByUserName(String userName);
    boolean existsByEmail(String email);
}
