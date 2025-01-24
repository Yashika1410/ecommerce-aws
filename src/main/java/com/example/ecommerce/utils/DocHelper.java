package com.example.ecommerce.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.ecommerce.common.enums.DocType;


public class DocHelper {
     private static final Pattern IMAGE_CONTENT_TYPE_PATTERN = Pattern.compile("^image/.*");

    // Method to check if a content type indicates an image
    public static boolean isImageContentType(String contentType) {
        // Use regex pattern to match content types starting with "image/"
        Matcher matcher = IMAGE_CONTENT_TYPE_PATTERN.matcher(contentType);
        return matcher.matches();
    }
    public static String getS3Path(DocType docType,String originalFileName,String uuid){
        String path = "";
        path+=docType.toString()+"/"+uuid+"/"+UUID.randomUUID()+"-"+originalFileName;
        return path;
    }
}
