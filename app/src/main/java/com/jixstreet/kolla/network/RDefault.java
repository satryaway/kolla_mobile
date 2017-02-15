package com.jixstreet.kolla.network;

/**
 * Default REST API Response structure.
 * All response from server must have status & message.
 * Some exceptions are old APIs that return array of objects. <br />
 * <br />
 * TODO: All REST API Response class must derive from this for easier handling in Http Client.
 *
 * @author Dwi C.
 */
public class RDefault {
    /**
     * Status string here must conform to one of {@link RStatus}.
     */
    public String status;

    public String message;
}
