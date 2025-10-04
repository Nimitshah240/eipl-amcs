package com.eipl.amcs.utils;

public class PatternUtil {

    public static String replaceFromDatabase(String str) {

        return str.replaceAll(AppConstant.DB_ENCODE_CHAR, "/");
    }

    public static String replaceToDatabase(String str) {

        return str.replaceAll("/", AppConstant.DB_ENCODE_CHAR);
    }

}
