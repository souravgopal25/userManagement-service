package com.chirp.userService.repository;

import com.chirp.userService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    //Optional<Boolean> existsUserBy(String email);
}
