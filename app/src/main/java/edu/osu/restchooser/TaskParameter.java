package edu.osu.restchooser;

import android.content.Context;

/**
 * Created by paranjay on 11/9/15.
 */
public class TaskParameter {
    String latitude;
    String longitude;
    Context context;

    TaskParameter(String latitude, String longitude, Context context) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }
}

