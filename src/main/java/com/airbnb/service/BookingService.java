package com.airbnb.service;

import com.airbnb.entity.Bookings;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.exception.ResourceNotFound;
import com.airbnb.repository.BookingsRepository;
import com.airbnb.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


@Service
public class BookingService {

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private SmsService smsService;

    public Bookings addBookings(Bookings bookings, PropertyUser user, long propertyId) {

        bookings.setPropertyUser(user);
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new ResourceNotFound("Property not found for this id!!"));

        Integer totalNights = bookings.getTotalNights();
        Integer nightlyPrice = property.getNightlyPrice();
        int totalPrice = totalNights * nightlyPrice;

        bookings.setProperty(property);
        bookings.setTotalPrice(totalPrice);

        Bookings saved = bookingsRepository.save(bookings);

        //Create PDF with Booking confirmation
        String pdfFilePath = "D://Booking-Confirm" + saved.getId() + ".pdf";
        boolean generatedPDF = pdfService.generatePDF(pdfFilePath, saved);
        if (generatedPDF) { //upload pdf on aws S3
            try {
                MultipartFile multipartFile = BookingService.convertFileToMultipartFile(pdfFilePath);
                String uploadFileUrl = bucketService.uploadFile(multipartFile, "airbnb-images");
                smsService.sendSms("+918078616571","Your Booking Is Confirmed! Click for more information!!"+uploadFileUrl);
            } catch (IOException exception) {
                throw new RuntimeException("Failed to convert file to MultipartFile", exception);
            }
        }
        return saved;
    }

    public static MultipartFile convertFileToMultipartFile(String pdfFilePath) throws IOException {
        File file = new File(pdfFilePath);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        return new MockMultipartFile(
                file.getName(), // originalFilename
                fileSystemResource.getFile().getName(), // name
                "application/pdf", // contentType
                fileSystemResource.getInputStream());
    }
}
