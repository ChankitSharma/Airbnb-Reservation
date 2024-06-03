package com.airbnb.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {

    private String username;
    private String password;

}
