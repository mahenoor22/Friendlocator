package com.example.mprojects.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    TextInputLayout lphone, lpass;
    Button signin, cancel;
    TextView forgot, register;
    String passInput, phoneInput;
    final Context context = this;
    EditText ephone,epass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lphone = findViewById(R.id.phone);
        lpass = findViewById(R.id.lpass);

        signin = findViewById(R.id.signin);
        cancel = findViewById(R.id.cancel);

        ephone=findViewById(R.id.ephone);
        epass=findViewById(R.id.epass);

        register = findViewById(R.id.register);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, register.class);
                startActivity(i);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             epass.setText("");
             ephone.setText("");
            }
        });
    }

    private boolean validatephone() {
        phoneInput = lphone.getEditText().getText().toString().trim();

        if (phoneInput.isEmpty()) {
            lphone.setError("Field can't be empty");
            return false;
        }  else {
            lphone.setError(null);
            return true;
        }
    }

    public void login(View view) {
        validatephone();
        passInput = lpass.getEditText().getText().toString().trim();
        Log.e("al","hii");

        if (passInput.isEmpty()) {
            lpass.setError("Field can't be empty");
        } else {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse(Config.HOST +"login.php").newBuilder();
                urlBuilder.addQueryParameter("phone", phoneInput);
                urlBuilder.addQueryParameter("password", passInput);

                final String url = urlBuilder.build().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        Log.e("Logindata",url);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    //txtInfo.setText(response.body().string());

                                    try {
                                        String data = response.body().string().trim();
                                        Log.e("Logindata",data);
                                        JSONArray jsonArray = new JSONArray(data);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        if (data!=null) {
                                            SharedPreferences sp = getSharedPreferences(Config.SPName, MODE_PRIVATE);
                                            SharedPreferences.Editor e = sp.edit();
                                            e.putString(Config.ID, jsonObject.getString("userid"));
                                            e.putString(Config.name, jsonObject.getString("name"));
                                            e.putBoolean(Config.STATUS, true);
                                            e.commit();
                                            Toast.makeText(Login.this, "Welcome "+sp.getString(Config.name,"Not Found")
                                                    , Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(Login.this, MainActivity.class);
                                            startActivity(i);
                                            finish();

                                        } else {
                                            Toast.makeText(Login.this, "Login Unsuccesful..Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    ;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
