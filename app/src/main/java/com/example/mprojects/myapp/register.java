package com.example.mprojects.myapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/*
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;*/

public class register extends AppCompatActivity {
    TextInputLayout username,phone,pass1,pass2;
    Button register,cancel;
    String phoneInput,passwordInput,passwordInput2;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    ".{6,20}" +               //at least 6 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.username);
        phone=findViewById(R.id.rphone);
        pass1=findViewById(R.id.pass1);
        pass2=findViewById(R.id.pass2);

        register=findViewById(R.id.register);
        cancel=findViewById(R.id.cancel);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    registerdb(v);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(register.this,Login.class);
                startActivity(i);
            }
        });

    }

    private boolean validatephone() {
        phoneInput = phone.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
            phone.setError("Field can't be empty");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }
    private boolean validateUsername() {
        String usernameInput = username.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            username.setError("Username too long");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        passwordInput = pass1.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            pass1.setError("Field can't be empty");
            return false;
        }
        if(pass2.getEditText().getText().toString().isEmpty()){
            pass2.setError("Field can't be empty");
            return false;

        }else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pass1.setError("Password too weak");
            return false;
        } else {
            pass1.setError(null);
            return true;
        }
    }

    public void registerdb(View view){
        validateUsername();
        validatephone();
        validatePassword();
        if(!passwordInput.equals(pass2.getEditText().getText().toString())){
            pass2.setError("Passwords doesnt match");
        }
        else{
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST+"register.php").newBuilder();
            urlBuilder.addQueryParameter("phone", phoneInput);
            urlBuilder.addQueryParameter("pass", passwordInput);
            urlBuilder.addQueryParameter("name", username.getEditText().getText().toString());

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Log.e("register",response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String s1=response.body().string().trim();
                                Log.e("register",s1);
                                if(s1.equals("success"))
                                {
                                    Intent i=new Intent(register.this,Login.class);
                                    startActivity(i);
                                    finish();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                };
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    } }}
