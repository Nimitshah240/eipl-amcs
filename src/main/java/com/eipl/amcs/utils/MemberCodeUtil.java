package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;

public class MemberCodeUtil {
    public static String getShortCode(String code) {
        return String.valueOf(Long.parseLong(code.substring(7)));
    }

    public static String getLongCode(String code) {
        return MainApp.identityDto.getSociety() + String.format("%04d", Long.parseLong(code));
    }
}
