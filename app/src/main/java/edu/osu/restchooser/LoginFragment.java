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

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends FragmentActivity implements View.OnClickListener {

    public LoginFragment() {
    }

    private EditText userNameEditableField;
    private EditText passwordEditableField;
    private static final String TAG = LoginFragment.class.getSimpleName();
    private DatabaseHelper dh;
    private final static String OPT_NAME="name";

    public void onResume(){
        super.onResume();
        Log.d(TAG, "on resume called");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        FragmentManager.enableDebugLogging(true);
        userNameEditableField=(EditText)findViewById(R.id.emailText);
        passwordEditableField=(EditText)findViewById(R.id.passwordText);
        android.view.View btnLogin=findViewById(R.id.loginBtn);
        android.view.View btnSignup=findViewById(R.id.signupBtn);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        Log.d(TAG, "activity created!!");

    }

    private void checkLogin() {
        String username = this.userNameEditableField.getText().toString();
        String password = this.passwordEditableField.getText().toString();
        this.dh = new DatabaseHelper(this);
        List<String> names = this.dh.selectAll(username, password);
        try
        {
            Thread.sleep(300);
        }
        catch (java.lang.InterruptedException ex)
        {
            System.out.println(ex.getMessage());
        }
        if (names.size() > 0) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(OPT_NAME, username);
            editor.commit();
            Log.d(TAG, "login successful for " + username);

            startActivity(new Intent(this, CreateFilters.class));
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
        this.dh.insert(username, password);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loginBtn:
                checkLogin();
                break;
            case R.id.signupBtn:
                addUser();
                break;

        }
    }
}
