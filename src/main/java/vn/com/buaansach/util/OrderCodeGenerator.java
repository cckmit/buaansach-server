package vn.com.buaansach.util;

import java.util.Date;

public final class OrderCodeGenerator {
    public static String generate(){
        return (new Date()).getTime() + "";
    }
}
