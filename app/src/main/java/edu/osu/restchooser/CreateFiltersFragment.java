package edu.osu.restchooser;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.beust.jcommander.JCommander;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateFiltersFragment extends FragmentActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

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
    Button searchFilterBtn;
    Button prevVisitedRestBtn;
    private ProgressBar pb;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    AccessTokenTracker accessTokenTracker;
    LoginButton loginButton;
    CallbackManager callbackManager;

    private static String yelpResponseString;
    private String mLatitudeText = "40.0028979";
    private String mLongitudeText = "-83.0150972";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_create_filters);

//        dollarSpinner = (Spinner) findViewById(R.id.dollarSpinner);
//        ArrayAdapter<CharSequence> dollarAdapter = ArrayAdapter.createFromResource(this,
//                R.array.DollarRatingArray, android.R.layout.simple_spinner_item);
//        dollarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dollarSpinner.setAdapter(dollarAdapter);
//        dollarSpinner.setOnItemSelectedListener(this);

        reviewSpinner = (Spinner) findViewById(R.id.reviewSpinner);
        ArrayAdapter<CharSequence> reviewAdapter = ArrayAdapter.createFromResource(this,
                R.array.ReviewRatingArray, android.R.layout.simple_spinner_item);
        reviewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewSpinner.setAdapter(reviewAdapter);
        reviewSpinner.setOnItemSelectedListener(this);

        distanceSpinner = (Spinner) findViewById(R.id.distanceSpinner);
        ArrayAdapter<CharSequence> distanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.DistanceFilterArray, android.R.layout.simple_spinner_item);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(distanceAdapter);
        distanceSpinner.setOnItemSelectedListener(this);

        cuisineEditableField = (EditText)findViewById(R.id.cuisineEditText);
        searchFilterBtn = (Button)findViewById(R.id.searchFilterBtn);
        searchFilterBtn.setOnClickListener(this);
        prevVisitedRestBtn = (Button)findViewById(R.id.prevVisitedRestBtn);
        prevVisitedRestBtn.setOnClickListener(this);

        buildGoogleApiClient();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            public void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                Log.d(TAG, "onCurrentAccessTokenChanged " + currentAccessToken);
                CreateFiltersFragment.this.logout(currentAccessToken);
                CreateFiltersFragment.this.finish();
            }
        };

        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException ex)
        {
            ex.printStackTrace();
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button_filter);
        //loginButton.setReadPermissions("user_friends");
    }

    public void logout(AccessToken currentAccessToken)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(CreateFiltersFragment.this);

        if(currentAccessToken == null)
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("name");
            editor.commit();
        }
        startActivity(new Intent(CreateFiltersFragment.this, LoginFragment.class));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        searchFilterBtn.setEnabled(true);
    }

    private void makeUseOfNewLocation(Location location) {
        mLatitudeText = Double.toString(location.getLatitude());
        mLongitudeText = Double.toString(location.getLongitude());
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.searchFilterBtn:
                cuisine = cuisineEditableField.getText().toString();
                Log.w(TAG, "filter search button clicked ");
                Log.w(TAG, dollarRating + "");
                Log.w(TAG, reviewRating + "");
                Log.w(TAG, distanceRange + "");
                Log.w(TAG, cuisine);
                boolean connected = checkInternet();
                searchFilterBtn.setEnabled(false);
                if(mLatitudeText != null && mLongitudeText != null)
                {
                    new GetRandomRestaurant().execute(new TaskParameter(mLatitudeText, mLongitudeText, this));
                }
                break;
            case R.id.prevVisitedRestBtn:
                Intent intent = new Intent(this, RestaurantsFragment.class);
                startActivity(intent);
//                finish();
                break;

        }
    }

    public boolean checkInternet() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Spinner selectedSpinner = (Spinner)parent;
        int viewId = selectedSpinner.getId();
        switch (viewId)
        {
//            case R.id.dollarSpinner:
//                Log.w(TAG, "dollar Spinner selected at position " + pos);
//                if (parent.getItemAtPosition(pos) != null)
//                {
//                    dollarRating = parent.getItemAtPosition(pos).toString();
//                }
//                break;
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

    private class GetRandomRestaurant extends AsyncTask<TaskParameter, Void, Response> {

        Context ctx;
        @Override
        protected Response doInBackground(TaskParameter... params) {
            YelpAPICLI yelpApiCli = new YelpAPICLI();
//            yelpApiCli.location = params[0].latitude;
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
            yelpResponseString = result.getBody();

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
//            String businessResponseJSON = this.searchByBusinessId(firstBusinessID.toString());

//            Log.w(TAG, yelpResponseString);
            Log.w(TAG, firstBusinessID);
            Intent intent = new Intent(ctx, YelpActivityFragment.class);
            intent.putExtra(Constants.BUSINESS_ID, firstBusinessID);
            intent.putExtra(Constants.DISTANCE_RANGE, distanceRange);
            intent.putExtra(Constants.REVIEW_RATING, reviewRating);
            intent.putExtra(Constants.CUISINE, cuisine);
            startActivity(intent);
//            finish();
        }
    }

}
