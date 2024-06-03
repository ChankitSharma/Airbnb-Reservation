package com.airbnb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "property_user")
public class PropertyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true, length = 150)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "user_role", nullable = false, length = 20)
    private String userRole;

    @JsonIgnore // While preparing a json object out of this entity this variable is not take.
    @Column(name = "password", nullable = false)
    private String password;

}