package com.airbnb.service;

import com.airbnb.entity.Images;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.exception.ResourceNotFound;
import com.airbnb.exception.S3BucketException;
import com.airbnb.repository.ImagesRepository;
import com.airbnb.repository.PropertyRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private BucketService bucketService;
    @Autowired
    private PropertyRepository propertyRepository;

    public List<Images> getImagesById(long propertyId) {
        return imagesRepository.findByPropertyId(propertyId);
    }

    public Images uploadImg(MultipartFile file, String bucketName, long propertyId, PropertyUser user) {

            if (!amazonS3.doesBucketExistV2(bucketName)) {
                throw new S3BucketException("Bucket not found: " + bucketName);
            }

           // String key = file.getOriginalFilename();

//            if (amazonS3.doesObjectExist(bucketName, key)) {
//                throw new S3BucketException("File already exists in bucket: " + key);
//            }

            String imgUrl = bucketService.uploadFile(file, bucketName);
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new ResourceNotFound("Property not present!!"));
            Images img = new Images();
            img.setImageUrl(imgUrl);
            img.setProperty(property);
            img.setPropertyUser(user);

            return imagesRepository.save(img);
    }

    public boolean deleteFile(String imageUrl, String bucketName) {
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            // Delete the file from S3 bucket
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            // Delete the file reference from the database
            imagesRepository.deleteByImageUrl(imageUrl);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}