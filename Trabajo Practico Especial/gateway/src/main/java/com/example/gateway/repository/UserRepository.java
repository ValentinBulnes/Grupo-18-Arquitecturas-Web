package com.example.gateway.repository;

import com.example.gateway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.authorities
        WHERE LOWER(u.username) = ?1
    """)
    Optional<User> findOneWithAuthoritiesByUsernameIgnoreCase(String username );
}
