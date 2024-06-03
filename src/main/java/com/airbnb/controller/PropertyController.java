package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {


    @Autowired
    private UserService userService;

    @GetMapping("/{locationName}")
    public ResponseEntity<List<Property>> getProperty(@PathVariable String locationName){
        List<Property> property = userService.getProperty(locationName);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }
}
