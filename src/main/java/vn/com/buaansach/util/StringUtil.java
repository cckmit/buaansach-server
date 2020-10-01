package vn.com.buaansach.util;

import java.text.Normalizer;

public final class StringUtil {
    public static String toASCII(String input) {
        try {
            return input.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a")
                    .replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e")
                    .replaceAll("ì|í|ị|ỉ|ĩ", "i")
                    .replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u")
                    .replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y")
                    .replaceAll("đ", "d");
        } catch (Exception e) {
            return normalizeString(input);
        }
    }

    public static String normalizeString(String input) {
        input = input.replaceAll("đ", "d");
        return Normalizer
                .normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
