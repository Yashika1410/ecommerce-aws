package com.example.ecommerce.common.configurations.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.ecommerce.common.exception.DataValidationException;
import com.example.ecommerce.utils.DocHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonClientService {
    private final AmazonS3 amazonS3;

    private PutObjectResult uploadMultipartFile(MultipartFile file, String bucket, String uploadKey) {
        try {
            if(DocHelper.isImageContentType(file.getContentType().toLowerCase())) {
                //IF image then add watermark
                return uploadByteToS3(file.getBytes(), bucket, uploadKey);
            } else {
                InputStream inputStream = file.getInputStream();
                String contentType = file.getContentType();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(contentType);
                objectMetadata.setContentLength(file.getSize()); // to prevent Amazon S3 from loading entire stream (user photo) in memory to compute size
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, objectMetadata);
                putObjectRequest.withStorageClass(StorageClass.Standard);
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
                log.info("Uploading file to S3. filename=" + file.getName() + ", filesize=" + file.getSize());

                PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

                IOUtils.closeQuietly(inputStream);

                return putObjectResult;
            }

        } catch (Exception e) {
            log.error("Error uploading image.", e);
            throw new DataValidationException(e.getMessage());
        }
    }


    public boolean uploadFile(MultipartFile multipartFile, String filePath) {
        PutObjectResult putObjectResult = this.uploadMultipartFile(multipartFile, S3BucketConfig.bucketName, filePath);
        return putObjectResult != null;
    }
    public boolean uploadByte(byte[] data,String filePath, String fileType){
        PutObjectResult putObjectResult = null;
        if(DocHelper.isImageContentType(fileType.toLowerCase())) {
            putObjectResult = this.uploadByteToS3(data, S3BucketConfig.bucketName, filePath);
        } else {
            putObjectResult = this.uploadByteToS3(data, S3BucketConfig.bucketName, filePath);
        }
        return putObjectResult!=null;
    }

    public boolean uploadByteWithoutWatermark(byte[] data,String filePath){
        PutObjectResult putObjectResult = this.uploadByteToS3(data,S3BucketConfig.bucketName, filePath);
        return putObjectResult!=null;
    }


    private PutObjectResult uploadByteToS3(byte[] data, String bucket, String uploadKey) {
        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(data.length);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, new ByteArrayInputStream(data), metadata);
            putObjectRequest.withStorageClass(StorageClass.Standard);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
            return putObjectResult;
        } catch (Exception e) {
            log.error("Error uploading image.", e);
            return null;
        }
    }


   
    public String getXmlFileAsString(String fileName) {
        try {
            URL url = new URL(S3BucketConfig.bucketUrl+fileName);
            try (InputStream inputStream = url.openStream();
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String content = bufferedReader.lines().collect(Collectors.joining(""));
                if (content.isEmpty()) {
                    throw new RuntimeException("No content retrieved from the URL: " + url.toString());
                }
                log.info("Fetched content length: {}", content.length());
                return content;
            }
        } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
    }

}