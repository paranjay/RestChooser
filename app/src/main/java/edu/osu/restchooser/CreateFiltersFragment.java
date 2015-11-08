package edu.osu.restchooser;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateFiltersFragment extends Fragment {

    public CreateFiltersFragment() {

    }

    private static final String TAG = CreateFiltersFragment.class.getSimpleName();

    private String dollarRating;
    private String reviewRating;
    private String distanceRange;
    private String cuisine;

    private EditText cuisineEditableField;
    private Spinner dollarSpinner;
    private Spinner reviewSpinner;
    private Spinner distanceSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_filters, container, false);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.searchFilterBtn:
                cuisine = cuisineEditableField.getText().toString();
                Log.w(TAG, "filter search button clicked ");
                Log.w(TAG, dollarRating + "");
                Log.w(TAG, reviewRating + "");
                Log.w(TAG, distanceRange + "");
                Log.w(TAG, cuisine);
                break;
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

    public void onNothingSelected(AdapterView<?> parent) {
    }

}
