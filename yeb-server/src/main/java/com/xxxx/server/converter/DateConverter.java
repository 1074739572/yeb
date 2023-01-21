package com.xxxx.server.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        try {
            if (StringUtils.hasText(source)){
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM--dd"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
