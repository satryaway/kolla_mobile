package com.jixstreet.kolla.network;

/**
 * List of allowed REST API response 'status'.
 *
 * @author satryaway@gmail.com
 */
public class RStatus {
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    public static final String PENDING = "PENDING";
    public static final String UNVERIFIED = "UNVERIFIED";
    public static final String SESSION_EXPIRED = "SESSION_EXPIRED";
    public static final String PASSCODE_EXPIRED = "PASSCODE_EXPIRED";

    // this is so far only used by niaga trx, but can also used by other API if desired.
    public static final String CANCEL = "CANCEL";
}
