
package com.example.mprojects.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class splashscreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        SharedPreferences sp=getSharedPreferences(Config.SPName,MODE_PRIVATE);
        final Boolean loginstatus=sp.getBoolean(Config.STATUS,false);

        Button b1=(Button)findViewById(R.id.b1);
        Log.e(Config.STATUS,loginstatus+"");
        Handler handler=new Handler();
        if(loginstatus) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            },3000);
        }
        else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(splashscreen.this, Login.class);
                    startActivity(i);
                    finish();
                }
            },3000);
        }
        }
}
