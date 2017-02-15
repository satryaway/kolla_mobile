package com.jixstreet.kolla.prefs;

/**
 * Shared preference keys.
 *
 * @author Dwi C
 */
public class PrefKey {
    // Prefix should *hint* type of object stored by particular key.
    // It's not required but greatly help code management.
    // BOOL_ = Boolean
    // STR_ = String
    // URI_ = Uri
    // OBJ_ = Object (GSON string-object)


    // ============================================================
    // Below are on KPrefs (kept on storage until app uninstalled)
    // ============================================================

    /**
     * Key for Previous phone number inputted by user.
     */
    public static final String STR_PREV_PHONE = "STR_PREV_PHONE";

    /**
     * Key for app code, can be modified through debug settings, otherwise fixed
     * on production.
     */
    public static final String STR_APP_CODE = "STR_APP_CODE";

    /**
     * Key for Base URL, can be modified through debug settings, otherwise fixed
     * on production.
     */
    public static final String STR_BASE_URL = "STR_BASE_URL";

    /**
     * Key for Base URL port, can be modified through debug settings, otherwise fixed
     * on production.
     */
    public static final String INT_BASE_URL_PORT = "INT_BASE_URL_PORT";

    /**
     * Key for Base URL for product image, can be modified through debug
     * settings, otherwise fixed on production.
     */
    public static final String STR_PRODUCT_IMAGE_URL = "STR_PRODUCT_IMAGE_URL";

    /**
     * Keys to indicate if home intro page has been shown.
     */
    public static final String BOOL_HOME_INTRO = "BOOL_HOME_INTRO";

    /**
     * Keys to indicate if home transaction panel guide has been shown.
     */
    public static final String BOOL_HOME_TRX_GUIDE = "BOOL_HOME_TRX_GUIDE";

    /**
     * Key for last time fullscreen popup displayed.
     */
    public static final String LONG_LAST_FS_POPUP_DISPLAYED = "LONG_LAST_FS_POPUP_DISPLAYED";


    // ======================================================================
    // Below are on CPrefs (cleared on every logout)
    // ======================================================================

    /**
     * Key for last time (date only) app update checked, used by Home.
     * This key must be reset every login.
     */
    public static final String LONG_LAST_UPDATE_CHECK = "LONG_LAST_UPDATE_CHECK";

    /**
     * Key for GCM registration id.
     */
    public static final String STR_GCM_REGID = "STR_GCM_REGID";

    /**
     * Key for App version at the time GCM registration id is obtained.
     */
    public static final String INT_GCM_REG_APPVER = "STR_GCM_REG_APPVER";

    /**
     * Key for Android OS build version at the time GCM registration id is
     * obtained.
     */
    public static final String INT_GCM_REG_OSVER = "STR_GCM_REG_OSVER";

    /**
     * Key for notification payload, can be JSON or simple string. Saved by notification handler.
     */
    public static final String STR_NOTIFICATION_PAYLOAD = "STR_NOTIFICATION_PAYLOAD";

    /**
     * Key for global route.
     */
    public static final String STR_ROUTE = "STR_ROUTE";

    /**
     * Key for global route target, normally filled with class name.
     */
    public static final String STR_ROUTE_TARGET = "STR_ROUTE_TARGET";

    /**
     * Key for DIMO QR api. Must be obtained from server. Could be empty.
     */
    public static final String STR_DIMO_QR_API_KEY = "STR_DIMO_QR_API_KEY";

    /**
     * Key for news footer flag. Honor user request to close footer until next login.
     */
    public static final String BOOL_NEWS_FOOTER_CLOSED = "BOOL_NEWS_FOOTER_CLOSED";

    /**
     * Keys to store referral code
     */
    public static final String STR_REFERRAL_CODE = "STR_REFERRAL_CODE";

    /**
     * Key for flagging top up intro
     */
    public static final String BOOL_TOPUP_INTRO = "BOOL_TOPUP_INTRO";
}
