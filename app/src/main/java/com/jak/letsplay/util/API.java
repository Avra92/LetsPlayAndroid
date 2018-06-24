package com.jak.letsplay.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class API {
    private static RequestQueue queue;
    private static Context context;

    public static synchronized void init(Context ctx) {
        if (context == null) {
            context = ctx;
        }
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
    }

    public static void call(String url, final Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener error) {
        init(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, listener, error) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Custom header
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.putAll(super.getHeaders());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>(params);
                if (super.getParams() != null)
                    map.putAll(super.getParams());
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers == null) {
                    // cant just set a new empty map because the member is final.
                    response = new NetworkResponse(
                            response.statusCode,
                            response.data,
                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                            response.notModified,
                            response.networkTimeMs);
                }

                return super.parseNetworkResponse(response);
            }
        };
        queue.add(request);
    }
}
