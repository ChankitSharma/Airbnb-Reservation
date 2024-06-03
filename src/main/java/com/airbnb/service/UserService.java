package com.airbnb.service;

import com.airbnb.entity.Images;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.exception.ResourceNotFound;
import com.airbnb.entity.PropertyUser;
import com.airbnb.payload.LoginDto;
import com.airbnb.payload.PropertyUserDto;
import com.airbnb.payload.ReviewDto;
import com.airbnb.repository.ImagesRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.repository.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PropertyUserRepository propertyUserRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    public PropertyUser createUser(PropertyUserDto propertyUserDto){
        PropertyUser user = new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
        user.setUserRole("USER_ROLE");
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(),BCrypt.gensalt(10)));
       return propertyUserRepository.save(user);
    }

    public PropertyUser addUser(PropertyUserDto propertyUserDto){
        PropertyUser user = new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
        user.setUserRole("ROLE_PROPERTY_OWNER");
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(),BCrypt.gensalt(10)));
        return propertyUserRepository.save(user);
    }

    public String veryLogin(LoginDto loginDto) {
        Optional<PropertyUser> user = propertyUserRepository.findByUsername(loginDto.getUsername());
        PropertyUser propertyUser = user.orElseThrow(() -> new ResourceNotFound("User not found!!"));
           if(BCrypt.checkpw(loginDto.getPassword(), propertyUser.getPassword())) {
              return jwtService.generateTocken(propertyUser);
           }
        return null;
    }

    public List<Property> getProperty(String locationName) {
        return propertyRepository.findByLocationName(locationName);
    }

    public Review addReview(long propertyId, ReviewDto reviewDto, PropertyUser user){
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new ResourceNotFound("Property not present!!"));

//        Optional<Property> optionalPropertyID = propertyRepository.findById(propertyId);
//        Property property = optionalPropertyID.orElseThrow(() -> new ResourceNotFound("Property not present!!"));

        Review review = new Review();
        review.setContent(reviewDto.getContent());
        review.setProperty(property);
        review.setPropertyUser(user);
        return reviewRepository.save(review);
    }
}