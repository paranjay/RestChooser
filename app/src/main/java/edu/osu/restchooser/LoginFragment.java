package edu.osu.restchooser;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends FragmentActivity implements View.OnClickListener {

    public LoginFragment() {
    }

    private EditText userNameEditableField;
    private EditText passwordEditableField;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        FragmentManager.enableDebugLogging(true);
        userNameEditableField=(EditText)findViewById(R.id.emailText);
        passwordEditableField=(EditText)findViewById(R.id.passwordText);
        android.view.View btnLogin=findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loginBtn:
                System.out.println("login button pressed");
                break;

        }
    }
}
