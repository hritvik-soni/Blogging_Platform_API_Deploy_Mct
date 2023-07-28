package com.hritvik.BloggingPlatformAPI.repository;


import com.hritvik.BloggingPlatformAPI.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPostRepository extends JpaRepository<Post,Long> {

    List<Post> findByUserUserName(String userName);

}