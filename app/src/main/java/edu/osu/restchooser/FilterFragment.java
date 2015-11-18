package edu.osu.restchooser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.util.Log;

/**
 * Created by Steven on 11/18/2015.
 */
public class FilterFragment extends Fragment{

    public FilterFragment() {

    }

    private String dollarRating;
    private String reviewRating;
    private String distanceRange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.saveFilterBtn:
                //save the filter to the database
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Spinner selectedSpinner = (Spinner)parent;
        int viewId = selectedSpinner.getId();
        switch (viewId)
        {
            case R.id.dollarSpinner:
                Log.w(TAG, "dollar Spinner selected at position " + pos);
                if (parent.getItemAtPosition(pos) != null)
                {
                    dollarRating = parent.getItemAtPosition(pos).toString();
                }

                break;
            case R.id.reviewSpinner:
                Log.w(TAG, "review Spinner selected at position " + pos);
                reviewRating = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.distanceSpinner:
                Log.w(TAG, "distance Spinner selected at position " + pos);
                distanceRange = parent.getItemAtPosition(pos).toString();
                break;
        }
    }
}
