package com.monitor.util;

public class SecurityUtil {

    public static boolean checkXSSValidity(String value) {
        if (StringUtil.isNotEmpty(value)) {
            if (value.indexOf("<") > -1 || value.indexOf(">") > -1 || value.indexOf("&") > -1 || value.indexOf("\"") > -1 || value.indexOf("\\/") > -1
                    || value.indexOf("\'") > -1 && value.indexOf("script") > -1) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkSQLValidity(String str) {
        str = str.toLowerCase();
        String badStr = "insert |select |delete |update |drop |union ";
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return false;
            }
        }
        return true;
    }
}
