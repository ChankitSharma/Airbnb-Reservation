package com.airbnb.payload;

import com.airbnb.entity.Property;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {

    private long id;
    @NotBlank(message = "Content cannot be blank, null or empty!!")
    private String content;
    private Property property; // Add nested Property object
    private PropertyUserDto propertyUser;
}