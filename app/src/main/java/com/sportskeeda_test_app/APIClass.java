package com.sportskeeda_test_app;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class APIClass {
    public static final String BASE_URL = "http://data.sportskeeda.com/";
    public static final String API_INPUT_VALUE = "feed.json";

    public static void ShowToastMessage(Context context, String message) {

        if (context != null) {

            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);

            toast.setGravity(Gravity.CENTER, 0, 0);

            toast.show();
        }

    }

    public static void VolleyExceptions(Activity parActivity, VolleyError volleyError) {

        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }

        APIClass.ShowToastMessage(parActivity, message);
    }
}
