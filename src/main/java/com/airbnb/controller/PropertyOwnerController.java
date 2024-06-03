package com.airbnb.controller;

import com.airbnb.entity.PropertyUser;
import com.airbnb.payload.PropertyUserDto;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/property-owner")
public class PropertyOwnerController {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyUserRepository propertyUserRepository;

    @PostMapping("/sign-up")
    public ResponseEntity<String> addPropertyOwner(@RequestBody PropertyUserDto propertyUserDto) {
        if(propertyUserRepository.existsByEmailOrUsername(propertyUserDto.getEmail(), propertyUserDto.getUsername())) {
            return new ResponseEntity<>("Email or UserName is already present!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PropertyUser user = userService.addUser(propertyUserDto);
        if(user!=null){
            return new ResponseEntity<>("User Successfully Registered!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
