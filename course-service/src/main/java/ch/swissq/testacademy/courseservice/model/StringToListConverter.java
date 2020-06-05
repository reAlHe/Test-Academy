package ch.swissq.testacademy.courseservice.model;

import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;
import java.util.stream.Collectors;

@Converter
public class StringToListConverter implements AttributeConverter<Set<Long>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Long> list) {
        List<String> longList = list.stream().map(Object::toString).collect(Collectors.toList());
        return String.join(",", longList);
    }

    @Override
    public Set<Long> convertToEntityAttribute(String joined) {
        return Arrays.stream(joined.split(",")).filter(value -> !StringUtils.isEmpty(value)).map(Long::parseLong).collect(Collectors.toSet());
    }

}