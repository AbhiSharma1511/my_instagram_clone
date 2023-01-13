package com.appdeb.myinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.appdeb.myinstagram.databinding.SignUpActivityBinding;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity {

    private SignUpActivityBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignUpActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Sign Up");


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(SignUpActivity.this);
                signUpUser();

            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /********************************************************/
        binding.llSignUp.setOnClickListener(new View.OnClickListener() {
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

    private void signUpUser() {

        String email = binding.edtSignUpEmail.getText().toString();
        String username = binding.edtSignUpUsername.getText().toString();
        String password = binding.edtSignUpPassword.getText().toString();
        
        if (email.equals("") || username.equals("") || password.equals("")) {
//            FancyToast.makeText(this, "Enter all required field!", FancyToast.LENGTH_LONG, FancyToast.DEFAULT, true);
            Toast.makeText(SignUpActivity.this, "Enter all required field!", Toast.LENGTH_SHORT).show();
        }
        else {
            final ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            progressDialog.setMessage("Signing up " + username);
            progressDialog.show();
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUpActivity.this, "Sign Up Successful " + user.getUsername() + "☺", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        Intent intent1 = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        progressDialog.dismiss();
                        finish();
                    } else {
                        FancyToast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }


}