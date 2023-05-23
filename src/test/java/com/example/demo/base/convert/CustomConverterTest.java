package com.example.demo.base.convert;

import com.example.demo.base.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class CustomConverterTest {

    @DisplayName("converter test set to string")
    @Test
    void t1() {
        //given
        CustomConverter converter = new CustomConverter();

        Set<Role> attribute = new HashSet<>();
        attribute.add(Role.ADMIN);
        attribute.add(Role.USER);

        //when
        String result = converter.convertToDatabaseColumn(attribute);

        //then
        Assertions.assertThat(result.contains("ROLE_USER")).isTrue();
        Assertions.assertThat(result.contains("ROLE_ADMIN")).isTrue();
    }

    @DisplayName("converter test string to set")
    @Test
    void t2() {
        //given
        CustomConverter converter = new CustomConverter();
        String data = "ROLE_USER,ROLE_ADMIN";

        //when
        Set<Role> result = converter.convertToEntityAttribute(data);

        //then
        Assertions.assertThat(result).containsExactly(Role.ADMIN, Role.USER);
    }

}