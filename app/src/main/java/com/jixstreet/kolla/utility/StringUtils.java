package com.jixstreet.kolla.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jixstreet.kolla.BuildConfig;

public class StringUtils {

    @NonNull
    public static final String combineUrlWithPort(String url, int port) {
        if (TextUtils.isEmpty(url))
            return "";
        // don't add port on standard port 80
        if (port == BuildConfig.STANDARD_PORT)
            return url;
        if (url.endsWith("/"))
            url = url.substring(0, url.length() - 1);
        return url + ":" + port + "/";
    }

    /**
     * Combine 2 urls, added forward slash as needed.
     * Prevent duplicate forward slash when combining urls.
     * Return empty String on error.
     */
    @NonNull
    public static final String combineUrl(@Nullable String url1, @Nullable String url2) {
        if (TextUtils.isEmpty(url1))
            return "";
        if (TextUtils.isEmpty(url2))
            return url1;
        if (!url1.endsWith("/"))
            url1 += "/";
        if (url2.startsWith("/")) {
            if (url2.length() == 1)
                return url1;
            url2 = url2.substring(1);
        }
        return url1 + url2;
    }

    /**
     * Split a CSV string into string array. Return empty array on null.
     */
    public static final String[] splitCSV(String csv) {
        if (csv == null)
            return new String[0];
        return csv.split("\\s*,\\s*");
    }

    /**
     * <p>
     * Right pad a String with a specified character.
     * </p>
     * Javadoc description is copied from <a href=
     * "http://www.docjar.com/html/api/org/apache/commons/lang/StringUtils.java"
     * >org.apache.commons.lang.StringUtils</a>
     * <p>
     * License owned by Apache. Credit goes to original author (see link).
     * </p>
     * Our implementation differs by using {@link StringBuilder}.
     * <p>
     * The String is padded to the size of <code>size</code>.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.rightPad(null, *, *)     = null
     * StringUtils.rightPad("", 3, 'z')     = "zzz"
     * StringUtils.rightPad("bat", 3, 'z')  = "bat"
     * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
     * StringUtils.rightPad("bat", 1, 'z')  = "bat"
     * StringUtils.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return right padded String or original String if no padding is
     * necessary, <code>null</code> if null String input
     */
    @Nullable
    public static String rightPad(String str, int size, char padChar) {
        if (str == null)
            return null;
        int pads = size - str.length();
        if (pads <= 0)
            return str; // returns original String when possible

        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < pads; i++) {
            sb.append(padChar);
        }

        return sb.toString();
    }

}
