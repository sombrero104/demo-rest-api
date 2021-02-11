package me.sombrero.demorestapi.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    // TODO Optional이란?
    Optional<Account> findByEmail(String username);

}
