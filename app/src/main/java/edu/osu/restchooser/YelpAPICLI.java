package edu.osu.restchooser;

/**
 * Created by paranjay on 11/8/15.
 */

import com.beust.jcommander.Parameter;

/**
 * Command-line interface for the sample Yelp API runner.
 */
public class YelpAPICLI {

    public static final String DEFAULT_TERM = "dinner";
    public static final String DEFAULT_LOCATION = "San Francisco, CA";
    public static final String DEFAULT_LATITUDE = "0.0";
    public static final String DEFAULT_LONGITUDE = "0.0";

    @Parameter(names = {"-q", "--term"}, description = "Search Query Term")
    public String term = DEFAULT_TERM;

    @Parameter(names = {"-l", "--location"}, description = "Location to be Queried")
    public String location = DEFAULT_LOCATION;
    public String latitude = DEFAULT_LATITUDE;
    public String longitude = DEFAULT_LONGITUDE;

}
