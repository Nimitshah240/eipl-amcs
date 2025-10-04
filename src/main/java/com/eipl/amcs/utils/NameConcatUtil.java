package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;

public class NameConcatUtil {
    public static String nameConcate(Member member) {
        String name = "";
        if (MainApp.locale.equals("en")) {
            name = member.getFirstName() + " " + (member.getMiddleName() != null ? member.getMiddleName() + " " : "")
                    + (member.getLastName() != null ? member.getLastName() : "");
        } else {

            name = member.getFirstNameLocal() + " " + (member.getMiddleNameLocal() != null ? member.getMiddleNameLocal() + " " : "")
                    + (member.getLastNameLocal() != null ? member.getLastNameLocal() : "");
        }
        return name;
    }

    public static String codeConcate(Society society, String memberCode) {
        return society.getCode() + String.format("%04d", Long.parseLong(memberCode));
    }
}
