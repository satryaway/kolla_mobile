package com.jixstreet.kolla.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by satryaway on 2/22/2017.
 * satryaway@gmail.com
 */

public class TextUtils {
    public static boolean isEmailValid(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
}
