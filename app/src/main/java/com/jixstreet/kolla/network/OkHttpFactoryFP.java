package com.jixstreet.kolla.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Centralized {@link OkHttpClient} instance using fingerprint SSL. <br />
 * This is the default http factory used throughout the app.
 *
 * @author satryaway@gmail.com
 */
@SuppressWarnings("FinalStaticMethod")
public class OkHttpFactoryFP {
    private static volatile OkHttpClient _staticInstance;

    public synchronized static final OkHttpClient instance() {
        if (_staticInstance == null) {
            _staticInstance = new OkHttpClient();

            // By default OkHttp use 10 seconds for all timeout.
            // To avoid race condition (where android client report timeout, while server report
            // success but AFTER android timeout), client timeout must always bigger than server timeout.
            _staticInstance.setConnectTimeout(300L, TimeUnit.SECONDS); // connect timeout
            _staticInstance.setReadTimeout(300L, TimeUnit.SECONDS);
            _staticInstance.setWriteTimeout(300L, TimeUnit.SECONDS);
        }
        return _staticInstance;
    }

}
