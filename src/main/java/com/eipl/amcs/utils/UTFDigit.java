package com.eipl.amcs.utils;

import java.math.BigDecimal;
import java.util.HashMap;

public class UTFDigit {

    public static HashMap<String, Character> utfDigit = new HashMap<>(10);

    static {
        utfDigit.put("gu", (char) 0x0AE6);
        utfDigit.put("hn", (char) 0x0966);
        utfDigit.put("ta", (char) 0x0BE6);
        utfDigit.put("tl", (char) 0x0C66);
        utfDigit.put("kn", (char) 0x0CE6);
        utfDigit.put("ml", (char) 0x0D66);
        utfDigit.put("bg", (char) 0x09E6);
        utfDigit.put("mt", (char) 0x0966);
        utfDigit.put("as", (char) 0x09E6);
        utfDigit.put("od", (char) 0x0B66);
        utfDigit.put("pn", (char) 0x0A66);
    }

    public UTFDigit() {

    }

    public static String getUTFLong(long d, String lang) {
        char digitZero = utfDigit.get(lang);
        String temp = String.valueOf(d);
        char[] utfBytes = temp.toCharArray();
        String text = "";
        char c;
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                text = text + String.valueOf(utfBytes[i]);
            } else {
                c = (char) (digitZero + (utfBytes[i] - '0'));
                text = text + String.valueOf(c);
            }
        }
        return text;
    }

    /**
     * @param d
     * @param lang
     * @param convert 0-do not convert, 1 convert
     * @return
     */
    public static String getUTFLong(long d, String lang, int convert) {
        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            String temp = String.valueOf(d);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                    text = text + String.valueOf(utfBytes[i]);
                } else {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                }
            }
            return text;
        } else return String.valueOf(d);
    }

    public static String getUTFInteger(int d, String lang) {
        char digitZero = utfDigit.get(lang);
        String temp = String.valueOf(d);
        char[] utfBytes = temp.toCharArray();
        String text = "";
        char c;
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                text = text + String.valueOf(utfBytes[i]);
            } else {
                c = (char) (digitZero + (utfBytes[i] - '0'));
                text = text + String.valueOf(c);
            }
        }
        return text;
    }

    public static String getUTFInteger(int d, String lang, int convert) {
        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            String temp = String.valueOf(d);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                    text = text + String.valueOf(utfBytes[i]);
                } else {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                }
            }
            return text;
        } else return String.valueOf(d);
    }

    public static String getUTFFloat(double d, String lang) {
        char digitZero = utfDigit.get(lang);
        String temp = String.valueOf(d);
        char[] utfBytes = temp.toCharArray();
        String text = "";
        char c;
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                text = text + String.valueOf(utfBytes[i]);
            } else {
                c = (char) (digitZero + (utfBytes[i] - '0'));
                text = text + String.valueOf(c);
            }
        }
        return text;
    }

    public static String getUTFBigDecimal(BigDecimal d, String lang) {
        char digitZero = utfDigit.get(lang);
        String temp = String.valueOf(d);
        if (temp == null || temp.equalsIgnoreCase("null"))
            return null;
        char[] utfBytes = temp.toCharArray();
        String text = "";
        char c;
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                text = text + String.valueOf(utfBytes[i]);
            } else {
                c = (char) (digitZero + (utfBytes[i] - '0'));
                text = text + String.valueOf(c);
            }
        }
        return text;
    }

    public static String getUTFFloat(double d, String lang, int convert) {
        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            String temp = String.valueOf(d);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                    text = text + String.valueOf(utfBytes[i]);
                } else {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                }
            }
            return text;
        } else return String.valueOf(d);
    }

    public static String getUTFString(String temp, String lang) {
        char digitZero = utfDigit.get(lang);
        char[] utfBytes = temp.toCharArray();
        String text = "";
        char c;
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                text = text + String.valueOf(utfBytes[i]);
            } else {
                c = (char) (digitZero + (utfBytes[i] - '0'));
                text = text + String.valueOf(c);
            }
        }
        return text;
    }

    public static String getUTFString(String temp, String lang, int convert) {
        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                    text = text + String.valueOf(utfBytes[i]);
                } else {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                }
            }
            return text;
        } else return temp;
    }

    public static String getUTFDate(String temp, String lang) {
        char digitZero = utfDigit.get(lang);
        char[] utfBytes = temp.toCharArray();
        String text = "";
        char c;
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                text = text + String.valueOf(utfBytes[i]);
            } else {
                c = (char) (digitZero + (utfBytes[i] - '0'));
                text = text + String.valueOf(c);
            }
        }
        return text;
    }

    public static String getUTFDate(String temp, String lang, int convert) {
        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                    text = text + String.valueOf(utfBytes[i]);
                } else {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                }
            }
            return text;
        } else return temp;
    }

    public static String getUTFDouble(double amount, String lang) {
        char digitZero = utfDigit.get(lang);
        String temp = String.valueOf(amount);
        char[] utfBytes = temp.toCharArray();
        String text = "";
        char c;
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                text = text + String.valueOf(utfBytes[i]);
            } else {
                c = (char) (digitZero + (utfBytes[i] - '0'));
                text = text + String.valueOf(c);
            }
        }
        return text;
    }

    public static String getUTFDouble(double amount, String lang, int convert) {
        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            String temp = String.valueOf(amount);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                    text = text + String.valueOf(utfBytes[i]);
                } else {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                }
            }
            return text;
        } else return String.valueOf(amount);
    }

    public static String getUTFDouble(String temp, String lang, int convert) {
        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] == '.' || utfBytes[i] == '-') {
                    text = text + String.valueOf(utfBytes[i]);
                } else {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                }
            }
            return text;
        } else return temp;
    }

    public static String getUTFStringTime(String temp, String lang, int convert) {

        if (temp.contains(":")) {
            temp = String.format("%02d", Integer.parseInt(temp.substring(0, temp.indexOf(":")))) + ":" + String.format("%02d", Integer.parseInt(temp.substring(temp.indexOf(":") + 1)));
        }

        if (convert == 1) {
            char digitZero = utfDigit.get(lang);
            char[] utfBytes = temp.toCharArray();
            String text = "";
            char c;
            for (int i = 0; i < utfBytes.length; i++) {
                if (utfBytes[i] != ':') {
                    c = (char) (digitZero + (utfBytes[i] - '0'));
                    text = text + String.valueOf(c);
                } else {
                    text = text + String.valueOf(utfBytes[i]);
                }
            }
            return text;
        } else return temp;
    }

    public static String getEngTime(String temp) {
        if (temp.contains(":")) {
            return String.format("%02d", Integer.parseInt(temp.substring(0, temp.indexOf(":")))) + ":" + String.format("%02d", Integer.parseInt(temp.substring(temp.indexOf(":") + 1)));
        }
        return temp;
    }

}