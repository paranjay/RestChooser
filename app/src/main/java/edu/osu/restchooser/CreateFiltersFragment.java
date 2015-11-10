package edu.osu.restchooser;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.beust.jcommander.JCommander;
import com.google.android.gms.games.snapshot.SnapshotContentsEntityCreator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateFiltersFragment extends FragmentActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener{

    public CreateFiltersFragment() {

    }

    private static final String TAG = CreateFiltersFragment.class.getSimpleName();
    public static final String BUSINESSID = "BUSINESSID";

    private String dollarRating;
    private String reviewRating;
    private String distanceRange;
    private String cuisine;

    private EditText cuisineEditableField;
    private Spinner dollarSpinner;
    private Spinner reviewSpinner;
    private Spinner distanceSpinner;
    Button searchFilterBtn;

    private static String yelpResponseString;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_filters);

        dollarSpinner = (Spinner) findViewById(R.id.dollarSpinner);
        ArrayAdapter<CharSequence> dollarAdapter = ArrayAdapter.createFromResource(this,
                R.array.DollarRatingArray, android.R.layout.simple_spinner_item);
        dollarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dollarSpinner.setAdapter(dollarAdapter);
        dollarSpinner.setOnItemSelectedListener(this);

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
                boolean connected = checkInternet();
                searchFilterBtn.setEnabled(false);
                new DownloadWebpageTask().execute(new TaskParameter("43202", this));

                break;
        }
    }

    public boolean checkInternet()
    {
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

    private class DownloadWebpageTask extends AsyncTask<TaskParameter, Void, Response> {

        Context ctx;
        @Override
        protected Response doInBackground(TaskParameter... params)
        {
            YelpAPICLI yelpApiCli = new YelpAPICLI();
            yelpApiCli.location = params[0].location;
            ctx = params[0].context;
            String [] arr = new String[]{};
            new JCommander(yelpApiCli, new String[]{});
            YelpAPI yelpApi = new YelpAPI();
            return yelpApi.queryAPI(yelpApiCli);
//            return request[0].send();
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
            JSONObject firstBusiness = (JSONObject) businesses.get(0);
            String firstBusinessID = firstBusiness.get("id").toString(); //randomize this
//            String businessResponseJSON = this.searchByBusinessId(firstBusinessID.toString());

//            Log.w(TAG, yelpResponseString);
            Log.w(TAG, firstBusinessID);
            Intent intent = new Intent(ctx, YelpActivityFragment.class);
            intent.putExtra(BUSINESSID, firstBusinessID);
            startActivity(intent);

        }
    }

}
