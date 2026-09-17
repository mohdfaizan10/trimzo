package com.trimzo.repository;

import com.trimzo.entity.ApiKey;
import com.trimzo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiKeyRepository
        extends JpaRepository<ApiKey, Long> {

    List<ApiKey> findByUserAndIsActiveTrue(User user);

    // Fetch user eagerly to avoid LazyInitializationException
    @Query("SELECT k FROM ApiKey k " +
            "JOIN FETCH k.user " +
            "WHERE k.isActive = true")
    List<ApiKey> findAllActiveWithUser();
}