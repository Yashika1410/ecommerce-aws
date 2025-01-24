package com.example.ecommerce.entities.converters;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return (Objects.nonNull(attribute) && !attribute.isEmpty()) ? String.join(SPLIT_CHAR, attribute) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return StringUtils.isNotBlank(dbData) ? Arrays.asList(dbData.split(SPLIT_CHAR)) : Collections.emptyList();
    }

}
