package com.eleven59.eleven59.config;

import org.springframework.core.convert.converter.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeToStringConverter implements Converter<LocalTime, String> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public String convert(LocalTime source) {
        return source != null ? source.format(formatter) : null;
    }
}
