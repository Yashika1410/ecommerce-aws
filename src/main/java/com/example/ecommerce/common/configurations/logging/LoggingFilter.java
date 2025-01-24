package com.example.ecommerce.common.configurations.logging;



import com.example.ecommerce.common.configurations.logging.entity.LoggingRequest;
import com.example.ecommerce.common.configurations.logging.entity.LoggingResponse;
import com.example.ecommerce.common.configurations.logging.wrapper.LoggingHttpServletRequestWrapper;
import com.example.ecommerce.common.configurations.logging.wrapper.LoggingHttpServletResponseWrapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    @Autowired
    ApiLoggingProperties apiLoggingProperties;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }

    private int maxContentSize;
    private Set<String> excludedPaths = emptySet();
    private String requestPrefix;
    private String responsePrefix;

    public LoggingFilter() {
        this(Builder.create());
    }

    public LoggingFilter(Builder builder) {
        requireNonNull(builder, "builder must not be null");
        this.maxContentSize = builder.maxContentSize;
        this.excludedPaths = builder.excludedPaths;
        this.requestPrefix = builder.requestPrefix;
        this.responsePrefix = builder.responsePrefix;
    }

    @Override
    public void init(FilterConfig filterConfig) {

        String maxContentSize = filterConfig.getInitParameter("maxContentSize");
        if (maxContentSize != null) {
            this.maxContentSize = Integer.parseInt(maxContentSize);
        }

        String excludedPaths = filterConfig.getInitParameter("excludedPaths");
        if (StringUtils.isNotBlank(excludedPaths)) {
            String[] paths = excludedPaths.split("\\s*,\\s*");
            this.excludedPaths = new HashSet<>(asList(paths));
        }

        String requestPrefix = filterConfig.getInitParameter("requestPrefix");
        if (StringUtils.isNotBlank(requestPrefix)) {
            this.requestPrefix = requestPrefix;
        }

        String responsePrefix = filterConfig.getInitParameter("responsePrefix");
        if (StringUtils.isNotBlank(responsePrefix)) {
            this.responsePrefix = responsePrefix;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        log.info("Processing Request: "+httpRequest.getRequestURI());
        if (request.getContentType() != null && request.getContentType().contains("multipart")) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        for (String excludedPath : excludedPaths) {
            String requestURI = httpRequest.getRequestURI();
            if (requestURI.startsWith(excludedPath)) {
                filterChain.doFilter(httpRequest, httpResponse);
                return;
            }
        }
        if(!httpRequest.getRequestURI().startsWith("/api")){
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        LoggingHttpServletRequestWrapper requestWrapper = new LoggingHttpServletRequestWrapper(httpRequest);
        LoggingHttpServletResponseWrapper responseWrapper = new LoggingHttpServletResponseWrapper(httpResponse);

        if (apiLoggingProperties.getLogging() != null && apiLoggingProperties.getLogging())
            log.info(requestPrefix + getRequestDescription(requestWrapper));
        filterChain.doFilter(requestWrapper, responseWrapper);
        if (apiLoggingProperties.getLogging() != null && apiLoggingProperties.getLogging())
            log.info(responsePrefix + getResponseDescription(responseWrapper));
        httpResponse.getOutputStream().write(responseWrapper.getContentAsBytes());
    }

    @Override
    public void destroy() {
    }

    protected String getRequestDescription(LoggingHttpServletRequestWrapper requestWrapper) {
        LoggingRequest loggingRequest = new LoggingRequest();
        loggingRequest.setSender(requestWrapper.getRemoteAddr());
        loggingRequest.setMethod(requestWrapper.getMethod());
        loggingRequest.setPath(requestWrapper.getRequestURI());
        loggingRequest.setParams(requestWrapper.isFormPost() ? null : requestWrapper.getParameters());
        loggingRequest.setHeaders(requestWrapper.getHeaders());
        String content = requestWrapper.getContent();
        try {
            loggingRequest.setBody(OBJECT_MAPPER.readValue(content, new TypeReference<Map<Object, Object>>() {
            }));
        } catch (IOException e) {
            loggingRequest.setBody(content);
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(loggingRequest);
        } catch (JsonProcessingException e) {
            log.warn("Cannot serialize Request to JSON", e);
            return null;
        }
    }

    protected String getResponseDescription(LoggingHttpServletResponseWrapper responseWrapper) {
        LoggingResponse loggingResponse = new LoggingResponse();
        loggingResponse.setStatus(responseWrapper.getStatus());
        loggingResponse.setHeaders(responseWrapper.getHeaders());
        String content = responseWrapper.getContent();
        try {
            loggingResponse.setBody(OBJECT_MAPPER.readValue(content, new TypeReference<Map<Object, Object>>() {
            }));
        } catch (IOException e) {
            log.warn("Error parsing response description.", e);
            loggingResponse.setBody(content);
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(loggingResponse);
        } catch (JsonProcessingException e) {
            log.warn("Cannot serialize Response to JSON", e);
            return null;
        }
    }

    // Register Filter
    @Bean
    public FilterRegistrationBean filterRegistrationBean(@Qualifier("dispatcherServletRegistration") ServletRegistrationBean registrationBean, LoggingFilter loggingFilter) {
        FilterRegistrationBean bean = new FilterRegistrationBean(loggingFilter);
        // Mapping filter to a Servlet
        bean.addServletRegistrationBeans(registrationBean);
        return bean;
    }

    public void onAuthenticationFailure(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        LoggingHttpServletRequestWrapper requestWrapper = new LoggingHttpServletRequestWrapper(httpRequest);
        LoggingHttpServletResponseWrapper responseWrapper = new LoggingHttpServletResponseWrapper(httpResponse);



        log.warn("onAuthenticationFailure "+requestPrefix + getRequestDescription(requestWrapper));
        log.warn("onAuthenticationFailure "+responsePrefix + getResponseDescription(responseWrapper));
    }

    public static class Builder {

        private String loggerName = LoggingFilter.class.getName();

        private int maxContentSize = 1024;

        private Set<String> excludedPaths = emptySet();

        private String requestPrefix = "REQUEST: ";

        private String responsePrefix = "RESPONSE: ";

        public static Builder create() {
            return new Builder();
        }

        public void loggerName(String loggerName) {
            requireNonNull(loggerName, "loggerName must not be null");
            this.loggerName = loggerName;
        }

        public Builder maxContentSize(int maxContentSize) {
            this.maxContentSize = maxContentSize;
            return this;
        }

        public Builder excludedPaths(String... excludedPaths) {
            requireNonNull(excludedPaths, "excludedPaths must not be null");
            this.excludedPaths = Stream.of(excludedPaths).collect(toSet());
            return this;
        }

        public void requestPrefix(String requestPrefix) {
            requireNonNull(requestPrefix, "requestPrefix must not be null");
            this.requestPrefix = requestPrefix;
        }

        public void responsePrefix(String responsePrefix) {
            requireNonNull(responsePrefix, "responsePrefix must not be null");
            this.responsePrefix = responsePrefix;
        }
    }

}