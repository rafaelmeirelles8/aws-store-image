package com.mySpringProjectImage.awsimageupload.buckets;

public enum BucketName {

    PROFILE_IMAGE("myproject-image-upload-123");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
