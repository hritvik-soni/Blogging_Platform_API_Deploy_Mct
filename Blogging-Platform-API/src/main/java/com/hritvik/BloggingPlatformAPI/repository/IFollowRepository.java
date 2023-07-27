package com.hritvik.BloggingPlatformAPI.repository;

import com.hritvik.BloggingPlatformAPI.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFollowRepository extends JpaRepository<Follow,Long> {


}