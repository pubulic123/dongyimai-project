package com.offcn.utils;

public class PhoneFormatCheckUtils {
    public static boolean isPhoneLegal(String phoneNum){
        String reg="^1[3-9][0-9]{11}";
        boolean matches = phoneNum.matches(reg);

        return matches;
    }
}
