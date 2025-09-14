package com.eleven59.eleven59.config;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public LocalTime convert(String source) {
        return source != null ? LocalTime.parse(source, formatter) : null;
    }
}
