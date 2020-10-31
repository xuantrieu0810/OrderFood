package com.lexuantrieu.orderfood.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class LibaryString {
    public static String covertStringToVN(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
