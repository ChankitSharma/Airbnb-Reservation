package com.airbnb.controller;

import com.airbnb.entity.Bookings;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.BookingService;
import com.airbnb.service.PDFService;
import com.airbnb.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping("/createBooking/{propertyId}")
    public ResponseEntity<Bookings> createBooking(@RequestBody Bookings bookings, @AuthenticationPrincipal PropertyUser user, @PathVariable long propertyId)
    {
        Bookings savedBookings = bookingService.addBookings(bookings, user, propertyId);
        return new ResponseEntity<>(savedBookings, HttpStatus.OK);
    }

}