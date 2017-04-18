package com.jixstreet.kolla.library;

import android.support.annotation.Nullable;

/**
 * Created by satryaway on 4/18/2017.
 * satryaway@gmail.com
 */

public interface Callback<P> {
    void run(@Nullable P param);
}
