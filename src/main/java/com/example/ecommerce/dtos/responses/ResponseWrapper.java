package com.example.ecommerce.dtos.responses;

import com.example.ecommerce.utils.ParserUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseWrapper {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Map<String, Object> data;

    public ResponseWrapper(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ResponseWrapper(boolean success, int code, String message, Map<String, Object> data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseWrapper getSuccessResponse(Map<String, Object> data, String message) {
        return new ResponseWrapper(true, HttpStatus.OK.value(), message, data);
    }

    public static ResponseWrapper convertObjectToResponseMapperForSuccess(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", message);
        response.put("data", data);
        response.put("success", true);
        return ParserUtils.extractObject(response, ResponseWrapper.class);
    }
}
