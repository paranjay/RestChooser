package edu.osu.restchooser;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Created by paranjay on 11/8/15.
 */
public class TwoStepOAuth extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}
