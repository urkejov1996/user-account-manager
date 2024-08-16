package com.example.useraccountmanager.repository;

import com.example.useraccountmanager.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(String email);


    Optional<User> findUserByEmail(@Email String email);
}
