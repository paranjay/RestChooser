package edu.osu.restchooser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends FragmentActivity implements
        FacebookCallback<LoginResult>,
        GraphRequest.GraphJSONObjectCallback{

    public LoginFragment() {
    }

    private EditText userNameEditableField;
    private EditText passwordEditableField;
    private static final String TAG = LoginFragment.class.getSimpleName();
    private DatabaseHelper dh;
    private final static String OPT_NAME="name";
    LoginButton loginButton;
    CallbackManager callbackManager;

    public void onResume(){
        super.onResume();
        Log.d(TAG, "on resume called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_login);
        FragmentManager.enableDebugLogging(true);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        AccessToken currentToken = AccessToken.getCurrentAccessToken();
        if(currentToken == null)
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(OPT_NAME);
            editor.commit();
        }

        if(settings.getString(OPT_NAME, null) != null)
        {
            startActivity(new Intent(this, CreateFiltersFragment.class));
            finish();
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        //loginButton.setReadPermissions("user_friends");

        loginButton.registerCallback(callbackManager, this);

    }

    @Override
    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
        try {
            String userName = jsonObject.get("name").toString();
            Log.w(TAG, userName);
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(OPT_NAME, userName);
            editor.commit();
            Log.d(TAG, "DONE with adding user name");
            startActivity(new Intent(this, CreateFiltersFragment.class));
            finish();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        // App code
        Log.w(TAG, "Facebook Login Success");

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        Log.w(TAG, "Facebook  Login Cancel");
    }

    @Override
    public void onError(FacebookException e) {
        Log.w(TAG, "Facebook Login Error");
        Log.w(TAG, e.getMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkLogin() {
        String username = this.userNameEditableField.getText().toString();
        String password = this.passwordEditableField.getText().toString();
        this.dh = new DatabaseHelper(this);
        List<String> names = this.dh.selectAllUsers(username, password);
        if (names.size() > 0) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(OPT_NAME, username);
            editor.commit();
            Log.d(TAG, "login successful for " + username);

            startActivity(new Intent(this, CreateFiltersFragment.class));
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Login failed")
                    .setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            Log.d(TAG, "login unsuccessful for " + username);
        }
    }

    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    private void addUser(){
        String username = this.userNameEditableField.getText().toString();
        String password = this.passwordEditableField.getText().toString();
        this.dh = new DatabaseHelper(this);
        this.dh.insertUser(username, password);
        new AlertDialog.Builder(this)
                .setTitle("Success!")
                .setMessage("User Added!")
                .setNeutralButton("Go back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        Log.d(TAG, "new user added as " + username);
    }

}
