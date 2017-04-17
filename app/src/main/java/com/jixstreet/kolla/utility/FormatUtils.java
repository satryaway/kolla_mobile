package com.jixstreet.kolla.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jixstreet.kolla.BuildConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by satryaway on 4/17/2017.
 * satryaway@gmail.com
 */


public class FormatUtils {
    @NonNull
    public static String formatCurrency(@Nullable String strInput) {
        return formatCurrency(strInput, BuildConfig.CURRENCY_STR + "0");
    }

    @NonNull
    public static String formatCurrency(@Nullable String strInput, @NonNull String defaultOutput) {
        if (strInput == null)
            return defaultOutput;
        try {
            return formatCurrency(new BigDecimal(strInput), defaultOutput);
        } catch (Exception ex) {
            return defaultOutput;
        }
    }

    @NonNull
    public static String formatCurrency(@Nullable BigDecimal input, @NonNull String defaultOutput) {
        if (input == null)
            return defaultOutput;
        try {
            BigDecimal display = input.setScale(2, RoundingMode.HALF_EVEN);
            NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
            nf.setMinimumFractionDigits(0);
            nf.setMaximumFractionDigits(2);
            return BuildConfig.CURRENCY_STR + nf.format(display.doubleValue());

        } catch (Exception ex) {
            return defaultOutput;
        }
    }
}
