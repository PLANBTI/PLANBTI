package com.example.demo.base.convert;

import com.example.demo.base.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class CustomConverter implements AttributeConverter<Set<Role>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Role> attribute) {
        return attribute.stream().map(i -> "ROLE_" + i.name()).collect(Collectors.joining(","));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(",")).map(i -> Role.valueOf(i.substring(5))).collect(Collectors.toSet());
    }
}
