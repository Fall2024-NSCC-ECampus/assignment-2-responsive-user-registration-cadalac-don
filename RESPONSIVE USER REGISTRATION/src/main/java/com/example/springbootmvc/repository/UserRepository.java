package com.example.springbootmvc.repository;

import com.example.springbootmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
