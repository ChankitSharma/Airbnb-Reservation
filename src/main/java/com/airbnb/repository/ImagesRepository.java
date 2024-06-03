package com.airbnb.repository;

import com.airbnb.entity.Images;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {

    List<Images> findByPropertyId(long propertyId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Images i WHERE i.imageUrl = :imageUrl")
    void deleteByImageUrl(@Param("imageUrl") String imageUrl);
}