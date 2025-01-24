package com.example.ecommerce.controller;


import com.example.ecommerce.common.constants.ResponseErrorCode;
import com.example.ecommerce.common.exception.AccountSuspendedException;
import com.example.ecommerce.common.exception.CustomAuthenticationException;
import com.example.ecommerce.common.exception.DataValidationException;
import com.example.ecommerce.common.exception.PermissionDeniedException;
import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.common.exception.UserInvalidException;
import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.MethodNotAllowedException;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

/**
 * This class is meant to be extended by all REST resource "controllers".
 * It contains exception mapping and other common REST API functionality
 */
@ControllerAdvice(annotations = RestController.class)
public abstract class ApiRestHandler implements ApplicationEventPublisherAware {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public @ResponseBody
    ResponseWrapper accessDeniedException(AccessDeniedException e) {
        log.warn("Access is denied : " + e.getMessage() +" "+ Arrays.toString(e.getStackTrace()));
        return new ResponseWrapper(false, HttpStatus.FORBIDDEN.value(), e.getMessage());
    }
    
    // @ResponseStatus(HttpStatus.FORBIDDEN)
    // @ExceptionHandler({ org.springframework.security.access.AccessDeniedException.class })
    // public @ResponseBody ResponseWrapper springAccessDeniedException(
    //         org.springframework.security.access.AccessDeniedException e) {
    //     log.warn("Access is denied : " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
    //     return new ResponseWrapper(false, HttpStatus.FORBIDDEN.value(), e.getMessage());
    // }
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // @ExceptionHandler({ Exception.class })
    // public @ResponseBody ResponseWrapper defaultException(Exception e) {
    //     log.warn("Exception Occured : " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
    //     return new ResponseWrapper(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error.");
    // }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    ResponseWrapper handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Invalid Arguments : " + e.getMessage() +" "+ Arrays.toString(e.getStackTrace()));
        return new ResponseWrapper(false, HttpStatus.BAD_REQUEST.value(), e.getFieldError().getDefaultMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public @ResponseBody ResponseWrapper handleInvalidFormatException(InvalidFormatException e) {
        log.warn("Invalid Arguments : " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        return new ResponseWrapper(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public @ResponseBody ResponseWrapper handleDataIntegrityViolationException(DataIntegrityViolationException de){
        log.warn("Data Integrity issue : "+de.getMessage()+" "+Arrays.toString(de.getStackTrace()));
        return new ResponseWrapper(false, HttpStatus.BAD_REQUEST.value(), de.getMessage());

    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestPartException.class})
    public @ResponseBody
    ResponseWrapper missingServletRequestPartException(MissingServletRequestPartException e) {
        log.warn("Missing Servlet Request Part : " + e.getMessage() +" "+ Arrays.toString(e.getStackTrace()));
        return new ResponseWrapper(false, ResponseErrorCode.BAD_DATA_FORMAT, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataValidationException.class)
    public @ResponseBody
    ResponseWrapper handleDataStoreException(DataValidationException ex, WebRequest request, HttpServletResponse response) {
        log.info("Data Validation Exception : " + ex.getMessage());
        return new ResponseWrapper(false, ResponseErrorCode.BAD_DATA_FORMAT, ex.getMessage(), ex.getData());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(PermissionDeniedException.class)
    public @ResponseBody
    ResponseWrapper permissionDeniedException(PermissionDeniedException ex, WebRequest request, HttpServletResponse response) {
        log.info("Permission Denied Exception : " + ex.getMessage());
        return new ResponseWrapper(false, HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage(), ex.getData());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public @ResponseBody
    ResponseWrapper handleDataStoreException(ResourceNotFoundException ex, WebRequest request, HttpServletResponse response) {
        log.info("Resource Not Found : " + ex.getMessage());
        return new ResponseWrapper(false, ResponseErrorCode.RESOURCE_NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserInvalidException.class)
    public @ResponseBody
    ResponseWrapper handleUserMisMatchException(UserInvalidException ex, WebRequest request, HttpServletResponse response) {
        log.info("User Mismatch Error : " + ex.getMessage());
        return new ResponseWrapper(false, ResponseErrorCode.USER_MISMATCH_ERROR,  ex.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowedException.class)
    public @ResponseBody
    ResponseWrapper handleMethodNotAllowedException(MethodNotAllowedException ex, WebRequest request, HttpServletResponse response) {
        log.info("Method Not Enabled : " + ex.getMessage());
        return new ResponseWrapper(false, HttpStatus.METHOD_NOT_ALLOWED.value(),  ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomAuthenticationException.class)
    public @ResponseBody
    ResponseWrapper handleAuthenticationException(CustomAuthenticationException ex, WebRequest request, HttpServletResponse response) {
        log.info("Authentication Exception : " + ex.getMessage());
        return new ResponseWrapper(false, ResponseErrorCode.AUTHENTICATION_ERROR, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccountSuspendedException.class)
    public @ResponseBody
    ResponseWrapper handleAccountSuspendedException(AccountSuspendedException ex, WebRequest request, HttpServletResponse response) {
        log.info("Account suspended Exception : " + ex.getMessage());
        return new ResponseWrapper(false, ResponseErrorCode.ACCOUNT_SUSPENDED_ERROR, ex.getMessage());
    }
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @ResponseBody
    ResponseWrapper handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request, HttpServletResponse response) {
        log.info("HTTP Method Not Supported : " + ex.getMessage());
        return new ResponseWrapper(false, HttpStatus.METHOD_NOT_ALLOWED.value(),  ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class})
    public @ResponseBody
    ResponseWrapper handleRunTimeException(RuntimeException e) {
        log.warn("Runtime Exception Handler : " + e.getMessage() +" "+ Arrays.toString(e.getStackTrace()));
        e.printStackTrace();
        return new ResponseWrapper(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error.");
    }
}