package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.util;

public class ReplaceUtil {

    public static String removeQuotes(String input) {
        if (input != null) {
            return input.replace("\"", "");
        }
        return input;
    }
}
