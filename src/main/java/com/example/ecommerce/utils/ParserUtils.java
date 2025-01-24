package com.example.ecommerce.utils;


import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ParserUtils {
    public  static <T> T extractObject(final Object object,final Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModules(new JtsModule(), new JavaTimeModule());
            return mapper.convertValue(object, type);
        }catch (final Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static <T> T extractObjectWithTypeReference(Object object, TypeReference<T> typeReference) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModules(new JtsModule(), new JavaTimeModule());
            return mapper.convertValue(object,typeReference);
        }catch (final Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static <T> T extractObjectFromString(String json,Class<T> type){
        try {
            return new ObjectMapper().readValue(json, type);
        } catch (Exception e) {
            log.error("An Error Occurred::",e);
        }
        return null;
    }

    public ResponseWrapper generateResponse(Map<String,Object> res, Integer code, Boolean success, String message){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(res);
        responseWrapper.setCode(code);
        responseWrapper.setSuccess(success);
        responseWrapper.setMessage(message);
        return responseWrapper;
    }

    public String generateRandomString(int timestampDigits, int randomStringDigit){
        String random = "";
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(randomStringDigit);

        for (int i = 0; i < randomStringDigit; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        String generatedString = sb.toString();
        Long currentTime = System.currentTimeMillis();
        random = generatedString + (long)(currentTime%Math.pow(10.0,timestampDigits));
        return random;
    }

    public static String mapToString(Object map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLogPrefix(Object obj,String methodName) {
        return obj.getClass().getName()+"."+methodName;
    }

    public  static String writeValueAsString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }catch (final Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static <T> T extractObjectFromString(String json,TypeReference<T> typeReference){
        try {
            return new ObjectMapper().readValue(json, typeReference);
        } catch (Exception e) {
            log.error("An Error Occurred::",e);
        }
        return null;
    }

    public static String toString(Object data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.warn("JSON Exception in toString", e);
        }
        return null;
    }

    public static boolean mapHasKeyValue(String key, Map<String, Object> data){
        return data.containsKey(key) && !StringUtils.isEmpty(data.get(key).toString());
    }
}
