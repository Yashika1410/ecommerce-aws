package com.example.ecommerce.common.configurations.logging.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class LoggingRequest implements Serializable {

    private static final long serialVersionUID = -4702574169916528738L;

    private String sender;

    private String method;

    private String path;

    private Map<String, String> params;

    private Map<String, String> headers;

    private Object body;
}
