package com.example.ecommerce.dtos.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateFileRequest {
    private List<Long> ids;
    @NotNull(message = "UUID should not be null")
    @NotBlank(message = "UUID should not be blank")
    private String uuid;
    @JsonProperty(defaultValue = "false", value = "isDoc")
    @Builder.Default
    private boolean isDoc = false;

}
