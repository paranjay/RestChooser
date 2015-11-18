package edu.osu.restchooser;

import android.os.AsyncTask;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;

import static java.lang.Thread.sleep;


/**
 * Created by Steven on 11/4/2015.
 */
public class YelpActivityFragment extends Fragment{

    public YelpActivityFragment() {

    }

    int

    public void onResume() {
        super.onResume();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_yelp, container, false);
        
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.soundsGoodBtn:
                chooseThisRest();
                break;
            case R.id.tryAgainBtn:
                tryAgain();
                break;
            case R.id.differentFilterBtn:
                useDifferentFilter();
                break;
            case R.id.saveFilterBtn:
                saveThisFilter();
                break;
            default:
                break;
        }
    }

    private void chooseThisRest() {
        //Move to map activity, insert this restaurant into db in list of already chosen restaurants
    }

    private void tryAgain() {

        new chooseNewRestaurantTask().execute();

        //Choose another random restaurant, then return to this activity
    }

    private void useDifferentFilter() {
        //Go back to createfilters activity
    }

    private void saveThisFilter() {
        //insert the filter used into the db for saved filters
    }

    private class chooseNewRestaurantTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params/* parameter describing filter */) {
            //query yelp with the filter, while sleeping in foreground
        }

        protected void onPostExecute() {
            //go to the yelp activity to show info
        }
    }
}
