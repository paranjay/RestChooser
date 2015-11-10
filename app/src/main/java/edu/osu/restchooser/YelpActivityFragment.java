package edu.osu.restchooser;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.beust.jcommander.JCommander;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.Response;

import static java.lang.Thread.sleep;


/**
 * Created by Steven on 11/4/2015.
 */
public class YelpActivityFragment extends FragmentActivity implements View.OnClickListener{

    public YelpActivityFragment() {

    }

    public void onResume() {
        super.onResume();
    }

    String businessID;
    TextView restName;

    Button soundsGoodBtn;
    Button tryAgainBtn;
    Button saveFilterBtn;
    Button differentFilterBtn;

    private static final String TAG = YelpActivityFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        businessID = intent.getStringExtra("BUSINESSID");
//        return inflater.inflate(R.layout.fragment_yelp, container, false);
        Log.w(TAG, businessID);
        setContentView(R.layout.fragment_yelp);

        restName = (TextView)findViewById(R.id.restName);
        restName.setText(businessID);

        soundsGoodBtn = (Button)findViewById(R.id.soundsGoodBtn);
        soundsGoodBtn.setOnClickListener(this);
        tryAgainBtn = (Button)findViewById(R.id.tryAgainBtn);
        tryAgainBtn.setOnClickListener(this);
        saveFilterBtn = (Button)findViewById(R.id.saveFilterBtn);
        saveFilterBtn.setOnClickListener(this);
        differentFilterBtn = (Button)findViewById(R.id.differentFilterBtn);
        differentFilterBtn.setOnClickListener(this);
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
        new DownloadWebpageTask().execute(new TaskParameter("43205", this));
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
            JSONObject firstBusiness = (JSONObject) businesses.get(0);
            String firstBusinessID = firstBusiness.get("id").toString(); //randomize this
//            String businessResponseJSON = this.searchByBusinessId(firstBusinessID.toString());

//            Log.w(TAG, yelpResponseString);
            Log.w(TAG, firstBusinessID);
            Intent intent = new Intent(ctx, YelpActivityFragment.class);
            intent.putExtra("BUSINESSID", firstBusinessID);
            startActivity(intent);

        }
    }


    private void useDifferentFilter() {
        startActivity(new Intent(this, CreateFiltersFragment.class));
    }

    private void saveThisFilter() {
        //insert the filter used into the db for saved filters
    }

}
