package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.dtos.requests.UpdateFileRequest;
import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.example.ecommerce.services.FileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.apache.http.impl.client.RequestWrapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class UploadDocsController extends ApiRestHandler{
    private final FileService fileService;
   @PostMapping
    public ResponseWrapper uploadFile(@RequestParam(name = "files") List<MultipartFile> files,@Valid @RequestParam(name = "uuid",required = true) String uuid) {
            return ResponseWrapper.getSuccessResponse(fileService.uploadFile(files,uuid),"successfully upload the file" );
    }

    @PutMapping
    public ResponseWrapper updatePosition(@Valid @RequestBody UpdateFileRequest requestWrapper) {
        return ResponseWrapper.getSuccessResponse(fileService.updatePosition(requestWrapper), "updated position");
    }
    @GetMapping("/{uuid}")
    public ResponseWrapper getFilesByUuid(@PathVariable String uuid) {
        return ResponseWrapper.getSuccessResponse(Collections.singletonMap("files", fileService.getFilesByUuid(uuid)), "fetched list of files by uuid");
    }
    @DeleteMapping("/{id}")
    public ResponseWrapper deleteFileById(@PathVariable Long id){
        fileService.deleteFileById(id);
        return ResponseWrapper
                .getSuccessResponse(null, "successfully deleted file");
    }
    
    
}
