package com.hust.server.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @program: yeb
 * @description: 全局日期格式转换
 * @author: Honors
 * @create: 2021-07-20 09:37
 */
//@Component
//public class DateConverter implements Converter<String, LocalDate> {
//    @Override
//    public LocalDate convert(String source) {
//        try {
//            return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}