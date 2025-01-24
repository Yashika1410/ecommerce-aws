package com.example.ecommerce.common.configurations.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3BucketConfig {
    public static String bucketName;
    public static String bucketUrl;

    @Value("${cloud.aws.s3.bucket.name}")
    public void setBucketName(String bucketName) {
        S3BucketConfig.bucketName = bucketName;
    }

    @Value("${cloud.aws.s3.bucket.url}")
    public void setBucketUrl(String bucketUrl) {
        S3BucketConfig.bucketUrl = bucketUrl;
    }
}
