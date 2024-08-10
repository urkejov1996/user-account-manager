package com.example.useraccountmanager.repository;

import com.example.useraccountmanager.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("select a from Account a where a.user.id = ?1")
    Optional<Account> findByUserId(String userId);
}
