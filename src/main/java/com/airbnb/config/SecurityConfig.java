package com.airbnb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JWTRequestFilter jwtRequestFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().cors().disable()
         .addFilterBefore(jwtRequestFilter,AuthorizationFilter.class);//It wil take authenticated request and call the JWTRequestFilter class for filltering the token

        httpSecurity.authorizeHttpRequests().anyRequest().permitAll();
//                .requestMatchers("/api/v1/properties/{locationName}","/api/v1/reviews/addReviews","/api/v1/reviews/getReviews").hasRole("USER")
//                .requestMatchers("/api/v1/images/upload/file","/api/v1/images/property/{propertyId}","/api/v1/images/delete/file").hasRole("ADMIN")
//
//               .requestMatchers("/api/v1/user/addUser","/api/v1/user/log-in").permitAll()
//               .requestMatchers("/api/countries/addCountry").hasRole("ADMIN")
//               .requestMatchers("/api/v1/user/profile").hasAnyRole("ADMIN","USER")
//               .anyRequest().authenticated();//It ensures that authorization rules are defined first
//

        return httpSecurity.build();
    }
}