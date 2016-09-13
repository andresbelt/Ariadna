package com.oncreate.ariadna.loginLearn;

import com.oncreate.ariadna.BuildConfig;
import com.oncreate.ariadna.Util.ConstantVariables;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

public class StringUtils {
    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return ConstantVariables.VERSION_NAME;
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public static boolean isNullOrWhitespace(String str) {
        if (str == null) {
            return true;
        }
        String[] a = str.split("\\s+");
        String s = ConstantVariables.VERSION_NAME;
        for (String anA : a) {
            s = s + anA;
        }
        if (s.length() != 0) {
            return false;
        }
        return true;
    }

    public static String insertWhitespace(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (i > 0 && i < s.length() - 1 && Character.isUpperCase(c)) {
                sb.append(' ');
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    public static String makeSpaces(int spaces) {
        return CharBuffer.allocate(spaces).toString().replace('\u0000', ' ');
    }

    public static String onlyLettersAndNumbers(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", ConstantVariables.VERSION_NAME);
    }

    public static int getSelectionLineNumber(String multiLines, int selection) {
        int count = 1;
        while (Pattern.compile("\n").matcher(multiLines.substring(0, selection)).find()) {
            count++;
        }
        return count;
    }
}
