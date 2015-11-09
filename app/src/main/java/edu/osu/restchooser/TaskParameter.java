package edu.osu.restchooser;

import android.content.Context;

/**
 * Created by paranjay on 11/9/15.
 */
public class TaskParameter {
    String location;
    Context context;

    TaskParameter(String location, Context context) {
        this.location = location;
        this.context = context;
    }
}

