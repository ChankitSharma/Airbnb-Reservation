package com.airbnb.repository;

import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.property =:property and r.propertyUser=:user")
    Review findReviewByUserIdAndPropertyId(@Param("property") Property property, @Param("user") PropertyUser user);

    List<Review> findByPropertyUser(PropertyUser user);

    @Modifying
    @Query("DELETE FROM Review r WHERE r.id = :id AND r.propertyUser = :user")
    int deleteReviewByIdAndUser(@Param("id") long id, @Param("user") PropertyUser user);

}