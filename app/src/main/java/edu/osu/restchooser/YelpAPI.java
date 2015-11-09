package edu.osu.restchooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public class YelpAPI {

    private static final String API_HOST = "api.yelp.com";
    private static final int SEARCH_LIMIT = 3;
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    private static final String TAG = YelpAPI.class.getSimpleName();

    private static final String CONSUMER_KEY = "QGwtPdCeIO5I_ySk-Xj0VA";
    private static final String CONSUMER_SECRET = "vKjPwyz4n4Fst1vKX4f73EetgbM";
    private static final String TOKEN = "y94KnRQwnF9wbmz_QIahrEr2FKZgy07v";
    private static final String TOKEN_SECRET = "BjaH7j2LC9yKTzEOEd155suxGz4";


    OAuthService service;
    Token accessToken;

    public YelpAPI() {
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                        .apiSecret(CONSUMER_SECRET).build();
        this.accessToken = new Token(TOKEN, TOKEN_SECRET);
    }

    public Response searchForBusinessesByLocation(String term, String location) {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
        return sendRequestAndGetResponse(request);
    }

    public Response searchByBusinessId(String businessID) {
        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
        return request;
    }



    private Response sendRequestAndGetResponse(OAuthRequest request) {
        System.out.println("Querying " + request.getCompleteUrl() + " ...");
        this.service.signRequest(this.accessToken, request);
        System.out.println(request);

        return  request.send();
//        return response.getBody();
    }

    public Response queryAPI(YelpAPICLI yelpApiCli) {
        return this.searchForBusinessesByLocation(yelpApiCli.term, yelpApiCli.location);
    }

    public static void main(String[] args) {
        YelpAPICLI yelpApiCli = new YelpAPICLI();
        new JCommander(yelpApiCli, args);

        YelpAPI yelpApi = new YelpAPI();
        yelpApi.queryAPI(yelpApiCli);
    }
}