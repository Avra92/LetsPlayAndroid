package com.jak.letsplay.util;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

public class FCMIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String username = Prefs.getInstance(getApplicationContext()).get(Constants.PREF_USERNAME);
        if (!username.isEmpty()) {
            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("token", refreshedToken);
            API.call(Constants.fcmTokenURL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }
}