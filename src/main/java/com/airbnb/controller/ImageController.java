package com.airbnb.controller;

import com.airbnb.entity.Images;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.ImagesRepository;
import com.airbnb.service.ImageService;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(path = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addImageUrl(@RequestParam MultipartFile file,
                                         @RequestParam String bucketName,
                                         @RequestParam long propertyId,
                                         @AuthenticationPrincipal PropertyUser user) {

        Images savedImages = imageService.uploadImg(file, bucketName, propertyId, user);

//        String imgUrl = bucketService.uploadFile(file, bucketName);
//        Property property = propertyRepository.findById(propertyId)
//                .orElseThrow(() -> new ResourceNotFound("Property not present!!"));
//        Images img = new Images();
//        img.setImageUrl(imgUrl);
//        img.setProperty(property);
//        img.setPropertyUser(user);
//        Images saved = imagesRepository.save(img);

        return new ResponseEntity<>(savedImages, HttpStatus.OK);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getAllImageUrl(@PathVariable long propertyId) {
        List<Images> images = imageService.getImagesById(propertyId);
        if (images.isEmpty()) {
            return new ResponseEntity<>("Images is not present!!",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

//    @DeleteMapping(path = "/delete/file/{bucketName}/{fileName}")
//    public ResponseEntity<String> deleteFile(@PathVariable String bucketName, @PathVariable String fileName) {
//        boolean isDeleted = imageService.deleteFile(bucketName, fileName);
//        if (isDeleted) {
//            return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("File deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping(path = "/delete/file")
    public ResponseEntity<String> deleteFile(@RequestParam String imageUrl,@RequestParam String bucketName) {
        boolean isDeleted = imageService.deleteFile(imageUrl,bucketName);
        if (isDeleted) {
            return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("File deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}