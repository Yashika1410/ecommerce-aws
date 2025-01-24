package com.example.ecommerce.common.configurations.logging;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ApiLoggingProperties {
    @Value("${api.logging:false}")
    private Boolean logging;

}
