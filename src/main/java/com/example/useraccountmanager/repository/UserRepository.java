package com.example.useraccountmanager.repository;

import com.example.useraccountmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(String email);


}
