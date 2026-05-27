package com.componentbid.review.repository;

import com.componentbid.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<User, UUID> {
}
