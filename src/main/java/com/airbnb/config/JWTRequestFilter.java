package com.airbnb.config;

import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private PropertyUserRepository propertyUserRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");//It will extract the token
        if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(8, tokenHeader.length() - 1);
//           System.out.println(token);
            String username = jwtService.getUsername(token);//From the token we will get the username after validating token.
            Optional<PropertyUser> optionalPropertyUserUser = propertyUserRepository.findByUsername(username);//Based on username we will get the user records form DB.
            // PropertyUserUser user = optionalPropertyUserUser.orElseThrow(() -> new ResourceNotFound("User not found!!"));
            if (optionalPropertyUserUser.isPresent()) {//It will check is the user present. (If yes)
                PropertyUser user = optionalPropertyUserUser.get();//It will get the user details and convert into entity class.
                //This three line will help us to -> keep track current user after logged in
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);//It will create a unique session-ID for that user which has logged in.
            }
        }
        filterChain.doFilter(request,response);
    }
}