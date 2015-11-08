package edu.osu.restchooser;

import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;


/**
 * Created by Steven on 11/4/2015.
 */
public class YelpActivityFragment extends Fragment{

    public YelpActivityFragment() {

    }

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

    }

    private void tryAgain() {

    }

    private void useDifferentFilter() {

    }

    private void saveThisFilter() {

    }
}
