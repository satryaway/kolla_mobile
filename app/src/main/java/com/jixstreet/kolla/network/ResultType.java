package com.jixstreet.kolla.network;

/**
 * Internal result type.
 *
 * @author satryaway@gmail.com
 */
public enum ResultType {
    /**
     * Android or library failure.
     */
    Fail,

    /**
     * Method call status. For REST API call need further check.
     */
    Success,

    /**
     * User canceled.
     */
    Cancel
}
