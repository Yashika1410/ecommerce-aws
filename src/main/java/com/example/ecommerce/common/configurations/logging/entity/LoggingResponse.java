package com.example.ecommerce.common.configurations.logging.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class LoggingResponse implements Serializable {

    private static final long serialVersionUID = -6692682176015358216L;

    private int status;

    private Map<String, String> headers;

    private Object body;
}
