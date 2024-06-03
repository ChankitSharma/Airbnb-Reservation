package com.airbnb.controller;

import com.airbnb.entity.PropertyUser;
import com.airbnb.payload.LoginDto;
import com.airbnb.payload.PropertyUserDto;
import com.airbnb.payload.TokenResponse;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyUserRepository propertyUserRepository;

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody PropertyUserDto propertyUserDto) {
        if(propertyUserRepository.existsByEmailOrUsername(propertyUserDto.getEmail(), propertyUserDto.getUsername())) {
            return new ResponseEntity<>("Email or UserName is already present!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PropertyUser user = userService.createUser(propertyUserDto);
        if(user!=null){
            return new ResponseEntity<>("User Successfully Registered!", HttpStatus.CREATED);
        }
            return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String token = userService.veryLogin(loginDto);
        if(token!=null){
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(token);

            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        }
            return new ResponseEntity<>("Invalid user Credential",HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public PropertyUser getCurrenntUserProfile(@AuthenticationPrincipal PropertyUser user){
        return user;
    }

}