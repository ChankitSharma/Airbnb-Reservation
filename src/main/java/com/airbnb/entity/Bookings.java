package com.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "total_price")
    private Integer totalPrice;

    @ManyToOne
    @JoinColumn(name = "property_user_id")
    private PropertyUser propertyUser;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "total_nights", nullable = false)
    private Integer totalNights;

}