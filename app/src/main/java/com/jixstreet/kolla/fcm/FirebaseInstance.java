package com.jixstreet.kolla.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.KollaApplication;
import com.jixstreet.kolla.prefs.CPrefs;
import com.jixstreet.kolla.utility.Log;

/**
 * Created by satryaway on 6/20/2017.
 * satryaway@gmail.com
 */

public class FirebaseInstance extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        CPrefs.write(KollaApplication.getContext(), CommonConstant.DEVICE_TOKEN_KEY,
                refreshedToken, String.class);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
