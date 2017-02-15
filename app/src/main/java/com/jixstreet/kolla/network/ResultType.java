package com.jixstreet.kolla.network;

/**
 * Internal result type.
 *
 * @author Dwi C.
 */
public enum ResultType {
    /**
     * Android or library failure.
     */
    Fail,

    /**
     * Method call success. For REST API call need further check.
     */
    Success,

    /**
     * User canceled.
     */
    Cancel
}
