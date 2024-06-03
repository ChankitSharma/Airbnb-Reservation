package com.airbnb.exception;

public class S3BucketException extends RuntimeException{
    public S3BucketException(String message) {
        super(message);
    }
}
