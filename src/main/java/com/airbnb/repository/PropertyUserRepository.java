package com.airbnb.repository;

import com.airbnb.entity.PropertyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PropertyUserRepository extends JpaRepository<PropertyUser, Long> {

    Optional<PropertyUser> findByUsername(String username);

    boolean existsByEmailOrUsername(String email,String username);

//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM PropertyUser u WHERE u.email = :email OR u.username = :username")
//    boolean existsByEmailOrUsername(@Param("email") String email, @Param("username") String username);

}