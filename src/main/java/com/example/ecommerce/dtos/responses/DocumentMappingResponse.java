package com.example.ecommerce.dtos.responses;

import com.example.ecommerce.common.configurations.aws.S3BucketConfig;
import com.example.ecommerce.entities.DocumentMapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class DocumentMappingResponse {
    private Long id;
    private String url;
    private int position;

    public String getUrl() {
        if (url != null) {
            return S3BucketConfig.bucketUrl + "" + url;
        }
        return null;
    }

    public static DocumentMappingResponse fromModel(DocumentMapping docManagement) {
        if (docManagement == null)
            return null;
        return new DocumentMappingResponse(docManagement.getId(), docManagement.getUrl(),
                docManagement.getPosition());
    }
}
