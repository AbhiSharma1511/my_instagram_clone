package com.appdeb.myinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.display.DeviceProductInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appdeb.myinstagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Log In");

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.edtUsername.getText().toString();
                String password = binding.edtPassword.getText().toString();

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Logging...");

                progressDialog.show();

                loginUser(username, password);
            }
        });

        binding.tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*************************************************/
        binding.llLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }


    private void loginUser(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) {
                    if (user.getBoolean("emailVerified")) {
                        FancyToast.makeText(LoginActivity.this, user.getUsername() + " logged in ☺", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
                        progressDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }else{
                        FancyToast.makeText(LoginActivity.this, "Please verified email first!", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                        progressDialog.dismiss();
                    }
                } else {
                    FancyToast.makeText(LoginActivity.this, "Error: " + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressDialog.dismiss();
                }
            }
        });
    }


}