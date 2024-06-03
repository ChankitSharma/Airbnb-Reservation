package com.airbnb.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String userRole;
    private String password;

}