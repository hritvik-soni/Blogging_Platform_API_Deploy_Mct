package com.hritvik.BloggingPlatformAPI.repository;


import com.hritvik.BloggingPlatformAPI.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ICommentRepository extends JpaRepository<Comment,Long> {

}