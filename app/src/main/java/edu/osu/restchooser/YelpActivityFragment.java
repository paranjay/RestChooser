package edu.osu.restchooser;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beust.jcommander.JCommander;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.Response;


/**
 * Created by Steven on 11/4/2015.
 */
public class YelpActivityFragment extends FragmentActivity implements View.OnClickListener ,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public YelpActivityFragment() {

    }

    public void onResume() {
        super.onResume();
    }

    String businessID;
    String reviewRating;
    String distanceRange;
    String cuisine;
    String restReviews;
    String restAddress;
    String restLatitude;
    String restLongitude;
    String restName;
    String restCuisine;
    String restRating;


    TextView restNameTxt;
    TextView restCuisineTxt;
    TextView restRatingTxt;
    TextView restNumReviewsTxt;
    TextView restLocationTxt;

    Button soundsGoodBtn;
    Button tryAgainBtn;
    Button saveFilterBtn;
    Button differentFilterBtn;

    private String mLatitudeText = "40.0028979";
    private String mLongitudeText = "-83.0150972";
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;


    private static final String TAG = YelpActivityFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        businessID = intent.getStringExtra(Constants.BUSINESS_ID);
        reviewRating = intent.getStringExtra(Constants.REVIEW_RATING);
        distanceRange = intent.getStringExtra(Constants.DISTANCE_RANGE);
        cuisine = intent.getStringExtra(Constants.CUISINE);


        Log.w(TAG, businessID);
        setContentView(R.layout.fragment_yelp);

        new GetRestaurantDetails().execute(businessID);

        restNameTxt = (TextView)findViewById(R.id.restName);
        restCuisineTxt = (TextView)findViewById(R.id.restCuisine);
        restRatingTxt = (TextView)findViewById(R.id.restRating);
        restNumReviewsTxt = (TextView)findViewById(R.id.restNumReviews);
        restLocationTxt = (TextView)findViewById(R.id.restLocation);

        soundsGoodBtn = (Button)findViewById(R.id.soundsGoodBtn);
        soundsGoodBtn.setOnClickListener(this);
        tryAgainBtn = (Button)findViewById(R.id.tryAgainBtn);
        tryAgainBtn.setOnClickListener(this);
        saveFilterBtn = (Button)findViewById(R.id.saveFilterBtn);
        saveFilterBtn.setOnClickListener(this);
        differentFilterBtn = (Button)findViewById(R.id.differentFilterBtn);
        differentFilterBtn.setOnClickListener(this);
    }

    private class GetRestaurantDetails extends AsyncTask<String, Void, Response> {

        @Override
        protected Response doInBackground(String... params)
        {
            YelpAPICLI yelpApiCli = new YelpAPICLI();
            String [] arr = new String[]{};
            new JCommander(yelpApiCli, new String[]{});
            YelpAPI yelpApi = new YelpAPI();
            return yelpApi.searchByBusinessId(params[0]);
        }

        @Override
        protected void onPostExecute(Response result) {
            String yelpResponseString = result.getBody();

            String searchResponseJSON = yelpResponseString;
            JSONParser parser = new JSONParser();
            JSONObject response = null;
            try {
                response = (JSONObject) parser.parse(searchResponseJSON);
            } catch (ParseException pe) {
                System.out.println("Error: could not parse JSON response:");
                System.out.println(searchResponseJSON);
                System.exit(1);
            }
            Log.w(TAG, yelpResponseString);
            try {

                restName = response.get("name").toString();
                JSONArray reviewsJsonArray = (JSONArray)response.get("reviews");
                restReviews = "";
                for(int i=0; i < reviewsJsonArray.size(); i++)
                {
                    JSONObject reviewJSONObj = (JSONObject)reviewsJsonArray.get(i);
                    restReviews += reviewJSONObj.get("excerpt") + "\n";
                }

                JSONArray categories = (JSONArray) response.get("categories");
                String categoriesText = "";
                for (int i = 0; i < categories.size(); i++) {
                    JSONArray category = (JSONArray) categories.get(i);
                    categoriesText += ",  " + category.get(0).toString();
                }

                JSONObject restLocationJSON = (JSONObject) response.get("location");
                Log.d(TAG, restLocationJSON.toJSONString());
                restLatitude = ((JSONObject) restLocationJSON.get("coordinate")).get("latitude").toString();
                restLongitude = ((JSONObject) restLocationJSON.get("coordinate")).get("longitude").toString();



                JSONArray restAddressJSONArr = (JSONArray) restLocationJSON.get("display_address");
                restAddress = "";
                for (int i = 0; i < restAddressJSONArr.size(); i++) {
                    restAddress += "\n" + restAddressJSONArr.get(i).toString();
                }
                restCuisine = categoriesText.substring(2);
                restCuisineTxt.setText(restCuisine);
                restNameTxt.setText(restName);
                restRating = response.get("rating").toString();
                restRatingTxt.setText(restRating);
                restNumReviewsTxt.setText(response.get("review_count").toString());
                restLocationTxt.setText(restAddress);

            }
            catch (Exception ex)
            {
                Log.e(TAG, "onPostExecute "  + ex.getMessage());
                new GetRandomRestaurant().execute(new TaskParameter(mLatitudeText, mLongitudeText, YelpActivityFragment.this));
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.soundsGoodBtn:
                chooseThisRest();
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("latitude", restLatitude);
                intent.putExtra("longitude", restLongitude);
                intent.putExtra("restName", restName);
                startActivity(intent);
                break;
            case R.id.tryAgainBtn:
                tryAgainBtn.setEnabled(false);
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
        DatabaseHelper dh = new DatabaseHelper(this);

        dh.insertRestaurant(businessID, restAddress, restRating, restCuisine, restReviews, restName);
        Toast.makeText(getApplicationContext(), "Restaurant Saved!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void tryAgain() {
        new GetRandomRestaurant().execute(new TaskParameter(mLatitudeText, mLongitudeText, this));
    }

    private class GetRandomRestaurant extends AsyncTask<TaskParameter, Void, Response> {

        Context ctx;
        @Override
        protected Response doInBackground(TaskParameter... params)
        {
            YelpAPICLI yelpApiCli = new YelpAPICLI();
            yelpApiCli.latitude = params[0].latitude;
            yelpApiCli.longitude = params[0].longitude;
            ctx = params[0].context;
            String [] arr = new String[]{};
            new JCommander(yelpApiCli, new String[]{});
            YelpAPI yelpApi = new YelpAPI();
            return yelpApi.queryAPI(yelpApiCli);
        }

        @Override
        protected void onPostExecute(Response result) {
            String yelpResponseString = result.getBody();

            String searchResponseJSON = yelpResponseString;
            JSONParser parser = new JSONParser();
            JSONObject response = null;
            try {
                response = (JSONObject) parser.parse(searchResponseJSON);
            } catch (ParseException pe) {
                System.out.println("Error: could not parse JSON response:");
                System.out.println(searchResponseJSON);
                System.exit(1);
            }

            JSONArray businesses = (JSONArray) response.get("businesses");
            int rand = (int)(Math.random() * businesses.size());
            JSONObject firstBusiness = (JSONObject) businesses.get(rand);
            String firstBusinessID = firstBusiness.get("id").toString(); //randomize this

            Log.w(TAG, yelpResponseString);
            Log.w(TAG, firstBusinessID);
            Intent intent = new Intent(ctx, YelpActivityFragment.class);
            intent.putExtra(Constants.BUSINESS_ID, firstBusinessID);
            intent.putExtra(Constants.DISTANCE_RANGE, distanceRange);
            intent.putExtra(Constants.REVIEW_RATING, reviewRating);
            intent.putExtra(Constants.CUISINE, cuisine);
            startActivity(intent);
            finish();

        }
    }

    private void useDifferentFilter() {
        startActivity(new Intent(this, CreateFiltersFragment.class));
    }

    private void saveThisFilter() {
        //insert the filter used into the db for saved filters
    }

}
